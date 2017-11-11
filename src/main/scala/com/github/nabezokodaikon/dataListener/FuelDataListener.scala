package com.github.nabezokodaikon.dataListener

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  PitModeDefineValue,
  RaceData,
  TelemetryData,
  TimingsData,
  FuelData
}
import com.github.nabezokodaikon.pcars2.UdpDataReader.toPitMode
import com.typesafe.scalalogging.LazyLogging

final case class FuelAccumulationData(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    fuelCapacity: Short,
    initialFuelLevel: Float,
    currentFuelLevel: Float,
    currentLap: Short,
    currentSector: Short,
    lastLap: Short,
    consumptionUntilPitIn: Option[Float],
    history: List[FuelConsumption],
    totalHistory: List[List[FuelConsumption]]
) extends LazyLogging {
  import FuelAccumulationData._

  def toFuelData(): FuelData = {
    val allHistory = (totalHistory :+ history).flatMap(_.map(_.value))
    val (lastConsumption, averageConsumption) = allHistory.length match {
      case length if (length > 0) =>
        val lastConsumption = allHistory.last.toRound(2)
        val averageConsumption = allHistory.sum.divide(length, 2)
        (lastConsumption, averageConsumption)
      case _ => ("-.--", "-.--")
    }

    FuelData(
      base = base,
      lastConsumption = lastConsumption,
      averageConsumption = averageConsumption
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
      mergeTelemetryData(data, udpData) match {
        case Some(nextData) => context.become(processing(nextData))
        case None => Unit
      }
    case udpData: TimingsData if (data.isMenu) =>
      val nextData = resetData(data)
      clientManager ! nextData.toFuelData()
      context.become(processing(nextData))
    case udpData: TimingsData if (data.isRestart) =>
      val nextData = resetData(data)
      clientManager ! nextData.toFuelData()
      context.become(processing(nextData))
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
      fuelCapacity = 0,
      initialFuelLevel = 0f,
      currentFuelLevel = 0f,
      currentLap = 0,
      currentSector = 0,
      lastLap = 0,
      consumptionUntilPitIn = None,
      history = List[FuelConsumption](),
      totalHistory = List[List[FuelConsumption]]()
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
      fuelCapacity = data.fuelCapacity,
      initialFuelLevel = data.initialFuelLevel,
      currentFuelLevel = data.currentFuelLevel,
      currentLap = data.currentLap,
      currentSector = data.currentSector,
      lastLap = data.lastLap,
      consumptionUntilPitIn = data.consumptionUntilPitIn,
      history = data.history,
      totalHistory = data.totalHistory
    )

  private def resetData(data: FuelAccumulationData): FuelAccumulationData =
    FuelAccumulationData(
      isMenu = data.isMenu,
      isPlaying = data.isPlaying,
      isRestart = data.isRestart,
      viewedParticipantIndex = data.viewedParticipantIndex,
      fuelCapacity = 0,
      initialFuelLevel = 0f,
      currentFuelLevel = 0f,
      currentLap = 0,
      currentSector = 0,
      lastLap = 0,
      consumptionUntilPitIn = None,
      history = List[FuelConsumption](),
      totalHistory = List[List[FuelConsumption]]()
    )

  private def mergeTelemetryData(
    data: FuelAccumulationData, telemetryData: TelemetryData
  ): Option[FuelAccumulationData] =
    telemetryData.participantinfo.viewedParticipantIndex match {
      case index if (index == data.viewedParticipantIndex) =>
        (data.initialFuelLevel, data.currentSector) match {
          case (initialFuelLevel, sector) if (initialFuelLevel == 0f && sector == 1) =>
            Some(FuelAccumulationData(
              isMenu = data.isMenu,
              isPlaying = data.isPlaying,
              isRestart = data.isRestart,
              viewedParticipantIndex = data.viewedParticipantIndex,
              fuelCapacity = telemetryData.carState.fuelCapacity,
              initialFuelLevel = telemetryData.carState.fuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = data.currentLap,
              currentSector = data.currentSector,
              lastLap = data.lastLap,
              consumptionUntilPitIn = None,
              history = List[FuelConsumption](),
              totalHistory = List[List[FuelConsumption]]()
            ))
          case (initialFuelLevel, _) if (initialFuelLevel == 0f) =>
            None
          case (initialFuelLevel, _) if (telemetryData.carState.fuelLevel > initialFuelLevel) =>
            Some(FuelAccumulationData(
              isMenu = data.isMenu,
              isPlaying = data.isPlaying,
              isRestart = data.isRestart,
              viewedParticipantIndex = data.viewedParticipantIndex,
              fuelCapacity = data.fuelCapacity,
              initialFuelLevel = telemetryData.carState.fuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = data.currentLap,
              currentSector = data.currentSector,
              lastLap = data.lastLap,
              consumptionUntilPitIn = data.consumptionUntilPitIn,
              history = data.history,
              totalHistory = data.totalHistory
            ))
          case _ =>
            Some(FuelAccumulationData(
              isMenu = data.isMenu,
              isPlaying = data.isPlaying,
              isRestart = data.isRestart,
              viewedParticipantIndex = data.viewedParticipantIndex,
              fuelCapacity = data.fuelCapacity,
              initialFuelLevel = data.initialFuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = data.currentLap,
              currentSector = data.currentSector,
              lastLap = data.lastLap,
              consumptionUntilPitIn = data.consumptionUntilPitIn,
              history = data.history,
              totalHistory = data.totalHistory
            ))
        }
      case _ =>
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = telemetryData.participantinfo.viewedParticipantIndex,
          fuelCapacity = 0,
          initialFuelLevel = 0f,
          currentFuelLevel = 0f,
          currentLap = 0,
          currentSector = 0,
          lastLap = 0,
          consumptionUntilPitIn = None,
          history = List[FuelConsumption](),
          totalHistory = List[List[FuelConsumption]]()
        ))
    }

  private def mergeTimingsData(
    data: FuelAccumulationData, timingsData: TimingsData
  ): Option[FuelAccumulationData] = {
    val participant = timingsData.participants(data.viewedParticipantIndex)
    if (participant.currentTime < 0) return None
    (participant.currentLap, participant.sector, toPitMode(participant.pitMode), data.consumptionUntilPitIn) match {
      case (currentLap, sector, _, _) if (currentLap == 1 && sector == 1 && (data.currentLap != 1 || data.currentSector != 1)) =>
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          fuelCapacity = data.fuelCapacity,
          initialFuelLevel = data.initialFuelLevel,
          currentFuelLevel = data.currentFuelLevel,
          currentLap = currentLap,
          currentSector = sector,
          lastLap = 0,
          consumptionUntilPitIn = None,
          history = List[FuelConsumption](),
          totalHistory = List[List[FuelConsumption]]()
        ))
      case (currentLap, sector, PitModeDefineValue.PIT_MODE_NONE, None) if (currentLap == data.currentLap + 1 && sector == 1) =>
        val initialRemaining = data.fuelCapacity * data.initialFuelLevel
        val currentRemaining = data.fuelCapacity * data.currentFuelLevel
        val totalConsumption = data.history.map(_.value).sum
        val lastConsumption = initialRemaining - (currentRemaining + totalConsumption)
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          fuelCapacity = data.fuelCapacity,
          initialFuelLevel = data.initialFuelLevel,
          currentFuelLevel = data.currentFuelLevel,
          currentLap = currentLap,
          currentSector = sector,
          lastLap = data.currentLap,
          consumptionUntilPitIn = data.consumptionUntilPitIn,
          history = data.history :+ FuelConsumption(data.currentLap, lastConsumption),
          totalHistory = data.totalHistory
        ))
      case (currentLap, sector, _, Some(consumptionUntilPitIn)) if (currentLap == data.currentLap + 1) =>
        val consumption = FuelConsumption(data.currentLap, consumptionUntilPitIn)
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          fuelCapacity = data.fuelCapacity,
          initialFuelLevel = data.initialFuelLevel,
          currentFuelLevel = data.currentFuelLevel,
          currentLap = currentLap,
          currentSector = sector,
          lastLap = data.currentLap,
          consumptionUntilPitIn = None,
          history = List[FuelConsumption](),
          totalHistory = data.totalHistory :+ (data.history :+ consumption)
        ))
      case (_, _, PitModeDefineValue.PIT_MODE_DRIVING_INTO_PITS, None) =>
        val initialRemaining = data.fuelCapacity * data.initialFuelLevel
        val currentRemaining = data.fuelCapacity * data.currentFuelLevel
        val totalConsumption = data.history.map(_.value).sum
        val lastConsumption = initialRemaining - (currentRemaining + totalConsumption)
        Some(FuelAccumulationData(
          isMenu = data.isMenu,
          isPlaying = data.isPlaying,
          isRestart = data.isRestart,
          viewedParticipantIndex = data.viewedParticipantIndex,
          fuelCapacity = data.fuelCapacity,
          initialFuelLevel = data.initialFuelLevel,
          currentFuelLevel = data.currentFuelLevel,
          currentLap = data.currentLap,
          currentSector = data.currentSector,
          consumptionUntilPitIn = Some(lastConsumption),
          lastLap = data.lastLap,
          history = data.history,
          totalHistory = data.totalHistory
        ))
      case _ => None
    }
  }
}
