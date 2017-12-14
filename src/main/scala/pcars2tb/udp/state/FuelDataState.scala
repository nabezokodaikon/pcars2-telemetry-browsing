package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.util.BigDecimalSupport._
import pcars2tb.udp.listener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  PitModeDefineValue,
  UdpData,
  RaceData,
  TelemetryData,
  TimingsData,
  FuelData
}
import pcars2tb.udp.listener.UdpDataReader.toPitMode

final object FuelDataState {
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

  def createInitialState(gameStateData: GameStateData): FuelDataState =
    FuelDataState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      fuelCapacity = 0,
      prevInitialFuelLevel = 0f,
      initialFuelLevel = 0f,
      currentFuelLevel = 0f,
      currentLap = 0,
      currentSector = 0,
      lastLap = 0,
      consumptionUntilPitIn = None,
      history = List[FuelConsumption](),
      totalHistory = List[List[FuelConsumption]]()
    )
}

final case class FuelDataState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    fuelCapacity: Short,
    prevInitialFuelLevel: Float,
    initialFuelLevel: Float,
    currentFuelLevel: Float,
    currentLap: Short,
    currentSector: Short,
    lastLap: Short,
    consumptionUntilPitIn: Option[Float],
    history: List[FuelConsumption],
    totalHistory: List[List[FuelConsumption]]
) extends LazyLogging {
  import FuelDataState._

  def createNextState(udpData: UdpData): (FuelDataState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetGameStateData(udpData)
        (nextState, None)
      case udpData: RaceData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(toUdpData()))
      case udpData: TelemetryData if (isPlaying) =>
        mergeTelemetryData(udpData) match {
          case Some(nextState) => (nextState, None)
          case None => (this, None)
        }
      case udpData: TimingsData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData))
      case udpData: TimingsData if (isRestart) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData))
      case udpData: TimingsData if (isPlaying) =>
        mergeTimingsData(udpData) match {
          case Some(nextState) => (nextState, Some(nextState.toUdpData()))
          case None => (this, None)
        }
      case _ => (this, None)
    }

  def toUdpData(): FuelData = {
    val allHistory = (totalHistory :+ history).flatMap(_.map(_.value))
    val (lastConsumption, averageConsumption) = allHistory.length match {
      case length if (length > 0) =>
        val lastConsumption = allHistory.last.toRound(2)
        val averageConsumption = allHistory.sum.divide(length, 2)
        (lastConsumption, averageConsumption)
      case _ => ("--.--", "--.--")
    }

    FuelData(
      base = base,
      lastConsumption = lastConsumption,
      averageConsumption = averageConsumption
    )
  }

  def resetGameStateData(gameStateData: GameStateData): FuelDataState =
    FuelDataState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = this.viewedParticipantIndex,
      fuelCapacity = this.fuelCapacity,
      prevInitialFuelLevel = this.prevInitialFuelLevel,
      initialFuelLevel = this.initialFuelLevel,
      currentFuelLevel = this.currentFuelLevel,
      currentLap = this.currentLap,
      currentSector = this.currentSector,
      lastLap = this.lastLap,
      consumptionUntilPitIn = this.consumptionUntilPitIn,
      history = this.history,
      totalHistory = this.totalHistory
    )

  def resetState(): FuelDataState =
    FuelDataState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      viewedParticipantIndex = this.viewedParticipantIndex,
      fuelCapacity = 0,
      prevInitialFuelLevel = 0f,
      initialFuelLevel = 0f,
      currentFuelLevel = 0f,
      currentLap = 0,
      currentSector = 0,
      lastLap = 0,
      consumptionUntilPitIn = None,
      history = List[FuelConsumption](),
      totalHistory = List[List[FuelConsumption]]()
    )

  def mergeTelemetryData(telemetryData: TelemetryData): Option[FuelDataState] =
    telemetryData.participantInfo.viewedParticipantIndex match {
      case index if (index == this.viewedParticipantIndex) =>
        (this.initialFuelLevel, this.currentSector) match {
          case (initialFuelLevel, sector) if (initialFuelLevel == 0f && sector == 1) =>
            Some(FuelDataState(
              isMenu = this.isMenu,
              isPlaying = this.isPlaying,
              isRestart = this.isRestart,
              viewedParticipantIndex = this.viewedParticipantIndex,
              fuelCapacity = telemetryData.carState.fuelCapacity,
              prevInitialFuelLevel = telemetryData.carState.fuelLevel,
              initialFuelLevel = telemetryData.carState.fuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = this.currentLap,
              currentSector = this.currentSector,
              lastLap = this.lastLap,
              consumptionUntilPitIn = None,
              history = List[FuelConsumption](),
              totalHistory = List[List[FuelConsumption]]()
            ))
          case (initialFuelLevel, _) if (initialFuelLevel == 0f) =>
            None
          case (initialFuelLevel, _) if (telemetryData.carState.fuelLevel > initialFuelLevel) =>
            Some(FuelDataState(
              isMenu = this.isMenu,
              isPlaying = this.isPlaying,
              isRestart = this.isRestart,
              viewedParticipantIndex = this.viewedParticipantIndex,
              fuelCapacity = this.fuelCapacity,
              prevInitialFuelLevel = this.initialFuelLevel,
              initialFuelLevel = telemetryData.carState.fuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = this.currentLap,
              currentSector = this.currentSector,
              lastLap = this.lastLap,
              consumptionUntilPitIn = this.consumptionUntilPitIn,
              history = this.history,
              totalHistory = this.totalHistory
            ))
          case _ =>
            Some(FuelDataState(
              isMenu = this.isMenu,
              isPlaying = this.isPlaying,
              isRestart = this.isRestart,
              viewedParticipantIndex = this.viewedParticipantIndex,
              fuelCapacity = this.fuelCapacity,
              prevInitialFuelLevel = this.prevInitialFuelLevel,
              initialFuelLevel = this.initialFuelLevel,
              currentFuelLevel = telemetryData.carState.fuelLevel,
              currentLap = this.currentLap,
              currentSector = this.currentSector,
              lastLap = this.lastLap,
              consumptionUntilPitIn = this.consumptionUntilPitIn,
              history = this.history,
              totalHistory = this.totalHistory
            ))
        }
      case _ =>
        Some(FuelDataState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          viewedParticipantIndex = telemetryData.participantInfo.viewedParticipantIndex,
          fuelCapacity = 0,
          prevInitialFuelLevel = 0f,
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

  def mergeTimingsData(timingsData: TimingsData): Option[FuelDataState] = {
    import PitModeDefineValue._
    val participant = timingsData.participants(this.viewedParticipantIndex)
    if (participant.currentTime < 0) return None
    (participant.currentLap, participant.sector, toPitMode(participant.pitMode), this.consumptionUntilPitIn) match {
      case (_, _, pitMode, _) if (pitMode == PIT_MODE_DRIVING_OUT_OF_GARAGE || pitMode == PIT_MODE_IN_GARAGE) =>
        Some(resetState())
      case (currentLap, sector, _, _) if (currentLap == 1 && sector == 1 && (this.currentLap != 1 || this.currentSector != 1)) =>
        Some(FuelDataState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          viewedParticipantIndex = this.viewedParticipantIndex,
          fuelCapacity = this.fuelCapacity,
          prevInitialFuelLevel = this.prevInitialFuelLevel,
          initialFuelLevel = this.initialFuelLevel,
          currentFuelLevel = this.currentFuelLevel,
          currentLap = currentLap,
          currentSector = sector,
          lastLap = 0,
          consumptionUntilPitIn = None,
          history = List[FuelConsumption](),
          totalHistory = List[List[FuelConsumption]]()
        ))
      case (currentLap, sector, pitMode, None) if (currentLap == this.currentLap + 1 && sector == 1 && (pitMode == PIT_MODE_NONE || pitMode == PIT_MODE_DRIVING_INTO_PITS)) =>
        val initialRemaining = this.fuelCapacity * this.initialFuelLevel
        val currentRemaining = this.fuelCapacity * this.currentFuelLevel
        val totalConsumption = this.history.map(_.value).sum
        val lastConsumption = initialRemaining - (currentRemaining + totalConsumption)
        Some(FuelDataState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          viewedParticipantIndex = this.viewedParticipantIndex,
          fuelCapacity = this.fuelCapacity,
          prevInitialFuelLevel = this.prevInitialFuelLevel,
          initialFuelLevel = this.initialFuelLevel,
          currentFuelLevel = this.currentFuelLevel,
          currentLap = currentLap,
          currentSector = sector,
          lastLap = this.currentLap,
          consumptionUntilPitIn = this.consumptionUntilPitIn,
          history = this.history :+ FuelConsumption(this.currentLap, lastConsumption),
          totalHistory = this.totalHistory
        ))
      case (currentLap, sector, _, Some(consumptionUntilPitIn)) if (currentLap == this.currentLap + 1) =>
        val consumption = FuelConsumption(this.currentLap, consumptionUntilPitIn)
        (this.prevInitialFuelLevel == this.initialFuelLevel) match {
          case true =>
            Some(FuelDataState(
              isMenu = this.isMenu,
              isPlaying = this.isPlaying,
              isRestart = this.isRestart,
              viewedParticipantIndex = this.viewedParticipantIndex,
              fuelCapacity = this.fuelCapacity,
              prevInitialFuelLevel = this.prevInitialFuelLevel,
              initialFuelLevel = this.initialFuelLevel,
              currentFuelLevel = this.currentFuelLevel,
              currentLap = currentLap,
              currentSector = sector,
              lastLap = this.currentLap,
              consumptionUntilPitIn = None,
              history = this.history :+ consumption,
              totalHistory = this.totalHistory
            ))
          case _ =>
            Some(FuelDataState(
              isMenu = this.isMenu,
              isPlaying = this.isPlaying,
              isRestart = this.isRestart,
              viewedParticipantIndex = this.viewedParticipantIndex,
              fuelCapacity = this.fuelCapacity,
              prevInitialFuelLevel = this.prevInitialFuelLevel,
              initialFuelLevel = this.initialFuelLevel,
              currentFuelLevel = this.currentFuelLevel,
              currentLap = currentLap,
              currentSector = sector,
              lastLap = this.currentLap,
              consumptionUntilPitIn = None,
              history = List[FuelConsumption](),
              totalHistory = this.totalHistory :+ (this.history :+ consumption)
            ))
        }
      case (_, _, PIT_MODE_IN_PIT, None) =>
        val initialRemaining = this.fuelCapacity * this.initialFuelLevel
        val currentRemaining = this.fuelCapacity * this.currentFuelLevel
        val totalConsumption = this.history.map(_.value).sum
        val lastConsumption = initialRemaining - (currentRemaining + totalConsumption)
        Some(FuelDataState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          viewedParticipantIndex = this.viewedParticipantIndex,
          fuelCapacity = this.fuelCapacity,
          prevInitialFuelLevel = this.prevInitialFuelLevel,
          initialFuelLevel = this.initialFuelLevel,
          currentFuelLevel = this.currentFuelLevel,
          currentLap = this.currentLap,
          currentSector = this.currentSector,
          consumptionUntilPitIn = Some(lastConsumption),
          lastLap = this.lastLap,
          history = this.history,
          totalHistory = this.totalHistory
        ))
      case _ => None
    }
  }
}

final case class FuelConsumption(
    lap: Short,
    value: Float
)
