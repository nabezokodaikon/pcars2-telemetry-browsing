package com.github.nabezokodaikon.dataListener

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  RaceData,
  TelemetryData,
  TimingsData,
  FuelData
}
import com.typesafe.scalalogging.LazyLogging

final case class FuelAccumulationData(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    currentLap: Short,
    currentRemaining: Float,
    lastLap: Short,
    history: List[FuelConsumption]
) {
  import FuelAccumulationData._

  def toFuelData(): FuelData = {
    val (lastConsumption, averageConsumption) = history.length match {
      case length if (length > 0) =>
        (history.last.value.toRound(1), (history.map(_.value).sum / length).toRound(1))
      case _ => ("-.-", "-.-")
    }

    FuelData(
      base,
      lastConsumption,
      averageConsumption
    )
  }
}

final object FuelAccumulationData {
  val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.FUEL_DATA,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )
}

final case class FuelConsumption(
    lap: Short,
    value: Float
)

final class FuelDataListener(clientManager: ActorRef)
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug("AggregateTimeListener preStart.");
  }

  override def postStop() = {
    logger.debug("AggregateTimeListener postStop.")
  }

  def receive(): Receive = {
    case udpData: GameStateData =>
      context.become(processing(createInitialData(udpData)))
  }

  private def processing(data: FuelAccumulationData): Receive = {
    case udpData: GameStateData =>
      val nextData = resetGameState(data, udpData)
      context.become(processing(nextData))
    case udpData: RaceData if (data.isMenu) =>
      val nextData = resetData(data)
      clientManager ! nextData.toFuelData()
      context.become(processing(nextData))
    case udpData: TelemetryData if (data.isPlaying) =>
      if (udpData.participantinfo.viewedParticipantIndex != data.viewedParticipantIndex) {
        val nextData = mergeTelemetryData(data, udpData)
        clientManager ! nextData.toFuelData()
        context.become(processing(nextData))
      }
    case udpData: TimingsData if (data.isPlaying) =>
      mergeTimingsData(data, udpData) match {
        case Some(nextData) =>
          clientManager ! nextData.toFuelData()
          context.become(processing(nextData))
        case None => Unit
      }
  }

  private def createInitialData(gameStateData: GameStateData): FuelAccumulationData =
    FuelAccumulationData(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      currentLap = 0,
      currentRemaining = 0f,
      lastLap = 0,
      history = List[FuelConsumption]()
    )

  private def resetGameState(
    data: FuelAccumulationData, gameStateData: GameStateData
  ): FuelAccumulationData =
    FuelAccumulationData(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = data.viewedParticipantIndex,
      currentLap = data.currentLap,
      currentRemaining = data.currentRemaining,
      lastLap = data.lastLap,
      history = data.history
    )

  private def resetData(data: FuelAccumulationData): FuelAccumulationData =
    FuelAccumulationData(
      isMenu = data.isMenu,
      isPlaying = data.isPlaying,
      isRestart = data.isRestart,
      viewedParticipantIndex = data.viewedParticipantIndex,
      currentLap = 0,
      currentRemaining = 0f,
      lastLap = 0,
      history = List[FuelConsumption]()
    )

  private def mergeTelemetryData(
    data: FuelAccumulationData, telemetryData: TelemetryData
  ): FuelAccumulationData =
    telemetryData.participantinfo.viewedParticipantIndex match {
      case index if (index == data.viewedParticipantIndex) =>
        FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          currentLap = data.currentLap,
          currentRemaining = telemetryData.carState.fuelCapacity * telemetryData.carState.fuelLevel,
          lastLap = data.lastLap,
          history = data.history
        )
      case _ =>
        FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = telemetryData.participantinfo.viewedParticipantIndex,
          currentLap = 0,
          currentRemaining = 0f,
          lastLap = 0,
          history = List[FuelConsumption]()
        )
    }

  private def mergeTimingsData(
    data: FuelAccumulationData, timingsData: TimingsData
  ): Option[FuelAccumulationData] = {
    val partcipant = timingsData.partcipants(data.viewedParticipantIndex)
    partcipant.currentLap match {
      case currentLap if (currentLap == 1) =>
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          currentLap = currentLap,
          currentRemaining = data.currentRemaining,
          lastLap = 0,
          history = List[FuelConsumption]()
        ))
      case currentLap if (currentLap == data.currentLap + 1) =>
        val totalConsumption = data.history.map(_.value).sum
        val lastConsumption = FuelConsumption(
          lap = data.currentLap,
          value = data.currentRemaining - totalConsumption
        )
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          currentLap = currentLap,
          currentRemaining = 0f,
          lastLap = data.currentLap,
          history = data.history :+ lastConsumption
        ))
      case _ => None
    }
  }
}
