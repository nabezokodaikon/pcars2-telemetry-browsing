package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.listener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  PitModeDefineValue,
  UdpData,
  TelemetryData,
  TimingsData,
  TelemetrySummary
}
import pcars2tb.udp.listener.UdpDataReader.toPitMode

final object TelemetrySummaryState {

  val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.TELEMETRY_SUMMARY,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )

  def createInitialState(gameStateData: GameStateData): TelemetrySummaryState =
    TelemetrySummaryState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      minOilTemp = 0,
      maxOilTemp = 0,
      minOilPressure = 0,
      maxOilPressure = 0,
      minWaterTemp = 0,
      maxWaterTemp = 0,
      minWaterPressure = 0,
      maxWaterPressure = 0,
      minFuelPressure = 0,
      maxFuelPressure = 0
    )
}

final case class TelemetrySummaryState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    minOilTemp: Int,
    maxOilTemp: Int,
    minOilPressure: Int,
    maxOilPressure: Int,
    minWaterTemp: Int,
    maxWaterTemp: Int,
    minWaterPressure: Int,
    maxWaterPressure: Int,
    minFuelPressure: Int,
    maxFuelPressure: Int
) extends LazyLogging {
  import TelemetrySummaryState._

  private def toUdpData(): TelemetrySummary =
    TelemetrySummary(
      base = base,
      minOilTempCelsius = minOilTemp.toString,
      maxOilTempCelsius = maxOilTemp.toString,
      minOilPressureKPa = minOilPressure,
      maxOilPressureKPa = maxOilPressure,
      minWaterTempCelsius = minWaterTemp.toString,
      maxWaterTempCelsius = maxWaterTemp.toString,
      minWaterPressureKPa = minWaterPressure,
      maxWaterPressureKPa = maxWaterPressure,
      minFuelPressureKPa = minFuelPressure,
      maxFuelPressureKPa = maxFuelPressure
    )

  private def resetState() =
    TelemetrySummaryState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      viewedParticipantIndex = 0,
      minOilTemp = 0,
      maxOilTemp = 0,
      minOilPressure = 0,
      maxOilPressure = 0,
      minWaterTemp = 0,
      maxWaterTemp = 0,
      minWaterPressure = 0,
      maxWaterPressure = 0,
      minFuelPressure = 0,
      maxFuelPressure = 0
    )

  private def resetGameStateData(gameStateData: GameStateData) =
    TelemetrySummaryState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = viewedParticipantIndex,
      minOilTemp = minOilTemp,
      maxOilTemp = maxOilTemp,
      minOilPressure = minOilPressure,
      maxOilPressure = maxOilPressure,
      minWaterTemp = minWaterTemp,
      maxWaterTemp = maxWaterTemp,
      minWaterPressure = minWaterPressure,
      maxWaterPressure = maxWaterPressure,
      minFuelPressure = minFuelPressure,
      maxFuelPressure = maxFuelPressure
    )

  private def mergeTelemetryData(telemetryData: TelemetryData) = {
    val participantInfo = telemetryData.participantInfo
    val carState = telemetryData.carState
    TelemetrySummaryState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      viewedParticipantIndex = participantInfo.viewedParticipantIndex,
      minOilTemp = if (minOilTemp == 0) carState.oilTempCelsius.toInt else minOilTemp min carState.oilTempCelsius.toInt,
      maxOilTemp = maxOilTemp max carState.oilTempCelsius.toInt,
      minOilPressure = if (minOilPressure == 0) carState.oilPressureKPa else minOilPressure min carState.oilPressureKPa,
      maxOilPressure = maxOilPressure max carState.oilPressureKPa,
      minWaterTemp = if (minWaterTemp == 0) carState.waterTempCelsius.toInt else minWaterTemp min carState.waterTempCelsius.toInt,
      maxWaterTemp = maxWaterTemp max carState.waterTempCelsius.toInt,
      minWaterPressure = if (minWaterPressure == 0) carState.waterPressureKpa else minWaterPressure min carState.waterPressureKpa,
      maxWaterPressure = maxWaterPressure max carState.waterPressureKpa,
      minFuelPressure = if (minFuelPressure == 0) carState.fuelPressureKpa else minFuelPressure min carState.fuelPressureKpa,
      maxFuelPressure = maxFuelPressure max carState.fuelPressureKpa
    )
  }

  private def inGarage(timingsData: TimingsData): Boolean = {
    import PitModeDefineValue._
    val participant = timingsData.participants(viewedParticipantIndex)
    toPitMode(participant.pitMode) match {
      case PIT_MODE_IN_GARAGE => true
      case _ => false
    }
  }

  def createNextState(udpData: UdpData): (TelemetrySummaryState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetGameStateData(udpData)
        (nextState, None)
      case _ => (this, None)
    }
}
