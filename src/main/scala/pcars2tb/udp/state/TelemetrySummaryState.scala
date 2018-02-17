package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.listener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  RaceData,
  GameStateData,
  PitModeDefineValue,
  UdpData,
  TelemetryData,
  TimingsData,
  EngineSummary,
  TelemetrySummary
}
import pcars2tb.udp.listener.UdpDataReader.toPitMode
import pcars2tb.util.BigDecimalSupport._

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

  val initialEngineState = EngineSummaryState(
    minOilTemp = 0,
    maxOilTemp = 0,
    minOilPressure = 0,
    maxOilPressure = 0,
    minWaterTemp = 0,
    maxWaterTemp = 0,
    minWaterPressure = 0,
    maxWaterPressure = 0,
    minFuelPressure = 0,
    maxFuelPressure = 0,
    minEngineSpeed = 0,
    maxEngineSpeed = 0,
    maxEngineTorque = 0,
    minEngineTorque = 0,
    minEnginePower = 0,
    maxEnginePower = 0
  )

  def createInitialState(gameStateData: GameStateData): TelemetrySummaryState =
    TelemetrySummaryState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      engine = initialEngineState
    )
}

final case class EngineSummaryState(
    minOilTemp: Int,
    maxOilTemp: Int,
    minOilPressure: Int,
    maxOilPressure: Int,
    minWaterTemp: Int,
    maxWaterTemp: Int,
    minWaterPressure: Int,
    maxWaterPressure: Int,
    minFuelPressure: Int,
    maxFuelPressure: Int,
    minEngineSpeed: Float,
    maxEngineSpeed: Float,
    minEngineTorque: Float,
    maxEngineTorque: Float,
    minEnginePower: Double,
    maxEnginePower: Double
)

final case class TelemetrySummaryState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    engine: EngineSummaryState
) extends LazyLogging {
  import TelemetrySummaryState._

  private def toUdpData(): TelemetrySummary =
    TelemetrySummary(
      base = base,
      engine = EngineSummary(
        minOilTempCelsius = engine.minOilTemp,
        maxOilTempCelsius = engine.maxOilTemp,
        minOilPressureKPa = engine.minOilPressure,
        maxOilPressureKPa = engine.maxOilPressure,
        minWaterTempCelsius = engine.minWaterTemp,
        maxWaterTempCelsius = engine.maxWaterTemp,
        minWaterPressureKPa = engine.minWaterPressure,
        maxWaterPressureKPa = engine.maxWaterPressure,
        minFuelPressureKPa = engine.minFuelPressure,
        maxFuelPressureKPa = engine.maxFuelPressure,
        minEngineSpeed = engine.minEngineSpeed.toRound(0),
        maxEngineSpeed = engine.maxEngineSpeed.toRound(0),
        minEngineTorque = engine.minEngineTorque.toRound(0),
        maxEngineTorque = engine.maxEngineTorque.toRound(0),
        minEnginePower = engine.minEnginePower.toRound(0),
        maxEnginePower = engine.maxEnginePower.toRound(0)
      )
    )

  private def resetState() =
    TelemetrySummaryState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      viewedParticipantIndex = 0,
      engine = initialEngineState
    )

  private def resetGameStateData(gameStateData: GameStateData) =
    TelemetrySummaryState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = viewedParticipantIndex,
      engine = EngineSummaryState(
        minOilTemp = engine.minOilTemp,
        maxOilTemp = engine.maxOilTemp,
        minOilPressure = engine.minOilPressure,
        maxOilPressure = engine.maxOilPressure,
        minWaterTemp = engine.minWaterTemp,
        maxWaterTemp = engine.maxWaterTemp,
        minWaterPressure = engine.minWaterPressure,
        maxWaterPressure = engine.maxWaterPressure,
        minFuelPressure = engine.minFuelPressure,
        maxFuelPressure = engine.maxFuelPressure,
        minEngineSpeed = engine.minEngineSpeed,
        maxEngineSpeed = engine.maxEngineSpeed,
        minEngineTorque = engine.minEngineTorque,
        maxEngineTorque = engine.maxEngineTorque,
        minEnginePower = engine.minEnginePower,
        maxEnginePower = engine.maxEnginePower
      )
    )

  private def mergeTelemetryData(telemetryData: TelemetryData) = {
    val participantInfo = telemetryData.participantInfo
    val carState = telemetryData.carState
    val tyre3 = telemetryData.tyre3
    TelemetrySummaryState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      viewedParticipantIndex = participantInfo.viewedParticipantIndex,
      engine = EngineSummaryState(
        minOilTemp = if (engine.minOilTemp == 0) carState.oilTempCelsius.toInt else engine.minOilTemp min carState.oilTempCelsius.toInt,
        maxOilTemp = engine.maxOilTemp max carState.oilTempCelsius.toInt,
        minOilPressure = if (engine.minOilPressure == 0) carState.oilPressureKPa else engine.minOilPressure min carState.oilPressureKPa,
        maxOilPressure = engine.maxOilPressure max carState.oilPressureKPa,
        minWaterTemp = if (engine.minWaterTemp == 0) carState.waterTempCelsius.toInt else engine.minWaterTemp min carState.waterTempCelsius.toInt,
        maxWaterTemp = engine.maxWaterTemp max carState.waterTempCelsius.toInt,
        minWaterPressure = if (engine.minWaterPressure == 0) carState.waterPressureKPa else engine.minWaterPressure min carState.waterPressureKPa,
        maxWaterPressure = engine.maxWaterPressure max carState.waterPressureKPa,
        minFuelPressure = if (engine.minFuelPressure == 0) carState.fuelPressureKPa else engine.minFuelPressure min carState.fuelPressureKPa,
        maxFuelPressure = engine.maxFuelPressure max carState.fuelPressureKPa,
        minEngineSpeed = engine.minEngineSpeed min tyre3.engineSpeed,
        maxEngineSpeed = engine.maxEngineSpeed max tyre3.engineSpeed,
        minEngineTorque = engine.minEngineTorque min tyre3.engineTorque,
        maxEngineTorque = engine.maxEngineTorque max tyre3.engineTorque,
        minEnginePower = engine.minEnginePower min tyre3.enginePower,
        maxEnginePower = engine.maxEnginePower max tyre3.enginePower
      )
    )
  }

  private def inGarage(timingsData: TimingsData): Boolean = {
    import PitModeDefineValue._
    val participant = timingsData.participants(viewedParticipantIndex)
    toPitMode(participant.pitMode) match {
      case PIT_MODE_IN_GARAGE => true
      case PIT_MODE_DRIVING_OUT_OF_GARAGE => true
      case PIT_MODE_UNKNOWN => true
      case _ => false
    }
  }

  def createNextState(udpData: UdpData): (TelemetrySummaryState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetGameStateData(udpData)
        (nextState, None)
      case udpData: RaceData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(toUdpData()))
      case udpData: TelemetryData if (isPlaying) =>
        val nextState = mergeTelemetryData(udpData)
        (nextState, Some(toUdpData()))
      case udpData: TimingsData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData))
      case udpData: TimingsData if (isRestart) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData))
      case udpData: TimingsData if (isPlaying) =>
        inGarage(udpData) match {
          case true =>
            val nextState = resetState()
            (nextState, Some(nextState.toUdpData))
          case _ =>
            (this, None)
        }
      case _ => (this, None)
    }
}
