package com.github.nabezokodaikon.pcars1

import spray.json._
import DefaultJsonProtocol._

object TelemetryJsonProtocol extends DefaultJsonProtocol {
  implicit val participantInfoStringsFormat = jsonFormat8(ParticipantInfoStrings)

  implicit val participantInfoStringsAdditionalFormat = jsonFormat5(ParticipantInfoStringsAdditional)

  implicit val gameStateDataFormat = jsonFormat3(GameStateData)
  implicit val participantInfoDataFormat = jsonFormat2(ParticipantInfoData)
  implicit val participantInfoFormat = jsonFormat7(ParticipantInfo)
  implicit val unfilteredInputDataFormat = jsonFormat4(UnfilteredInputData)
  implicit val eventInfoDataFormat = jsonFormat2(EventInfoData)
  implicit val timingInfoDataFormat = jsonFormat10(TimingInfoData)
  implicit val sectorTimeDataFormat = jsonFormat12(SectorTimeData)
  implicit val flagDataFormat = jsonFormat2(FlagData)
  implicit val pitInfoDataFormat = jsonFormat2(PitInfoData)
  implicit val carStateDataFormat = jsonFormat22(CarStateData)
  implicit val carStateVecotrDataFormat = jsonFormat7(CarStateVecotrData)
  implicit val TyresDataFormat = jsonFormat22(TyresData)
  implicit val extraDataFormat = jsonFormat2(ExtraData)
  implicit val carDamageDataFormat = jsonFormat3(CarDamageData)
  implicit val weatherDataFormat = jsonFormat6(WeatherData)
  implicit val telemetryDataFormat = jsonFormat21(TelemetryData)
}

import TelemetryJsonProtocol._

case class FrameInfo(
  frameTypeAndSequence: Int,
  frameType: Int,
  sequence: Int)

// 1,347 Byte
case class ParticipantInfoStrings(
  frameType: Int = TelemetryDataConst.PARTICIPANT_INFO_STRINGS_FRAME_TYPE,
  buildVersionNumber: Int,
  packetType: Int,
  carName: String, // 64
  carClassName: String, // 64
  trackLocation: String, // 64
  trackVariation: String, // 64
  nameString: Array[String] // 16
) {
  def toJsonString: String = this.toJson.toString
}

// 1,028 Byte
case class ParticipantInfoStringsAdditional(
  frameType: Int = TelemetryDataConst.PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE,
  buildVersionNumber: Int,
  packetType: Int,
  offset: Int,
  name: Array[String] // 16
) {
  def toJsonString: String = this.toJson.toString
}

/*
 *  TelemetryData
 */
case class GameStateData(
  gameState: Long,
  sessionState: Long,
  raceStateFlags: Long)

case class ParticipantInfoData(
  viewedParticipantIndex: Byte, // [ UNSET = -1 ]
  numParticipants: Byte // [ UNSET = -1 ]
) {
  def toJsonString: String = this.toJson.toString
}

case class ParticipantInfo(
  worldPosition: Array[Short], // 3
  currentLapDistance: Int,
  racePosition: Int,
  lapsCompleted: Int,
  currentLap: Int,
  sector: Int,
  lastSectorTime: Float) {
  def toJsonString: String = this.toJson.toString
}

case class UnfilteredInputData(
  unfilteredThrottle: Float,
  unfilteredBrake: Float,
  unfilteredSteering: Float,
  unfilteredClutch: Float) {
  def toJsonString: String = this.toJson.toString
}

case class EventInfoData(
  lapsInEvent: Int,
  trackLength: Float // [ UNITS = Metres ]
) {
  def toJsonString: String = this.toJson.toString
}

case class TimingInfoData(
  lapInvalidated: Boolean,
  bestLapTime: Float, // [ UNITS = seconds ]
  lastLapTime: Float, // [ UNITS = seconds ]
  currentTime: Float, // [ UNITS = seconds ]
  splitTimeAhead: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  splitTimeBehind: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  splitTime: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  eventTimeRemaining: Float, // [ UNITS = milli-seconds ]
  personalFastestLapTime: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  worldFastestLapTime: Float // [ UNITS = seconds ] [ UNSET = -1.0f ]
) {
  def toJsonString: String = this.toJson.toString
}

case class SectorTimeData(
  currentSector1Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  currentSector2Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  currentSector3Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  fastestSector1Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  fastestSector2Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  fastestSector3Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  personalFastestSector1Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  personalFastestSector2Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  personalFastestSector3Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  worldFastestSector1Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  worldFastestSector2Time: Float, // [ UNITS = seconds ] [ UNSET = -1.0f ]
  worldFastestSector3Time: Float // [ UNITS = seconds ] [ UNSET = -1.0f ]
) {
  def toJsonString: String = this.toJson.toString
}

case class FlagData(
  highestFlagColor: Int,
  highestFlagReason: Int) {
  def toJsonString: String = this.toJson.toString
}

case class PitInfoData(
  pitMode: Int,
  pitSchedule: Int) {
  def toJsonString: String = this.toJson.toString
}

case class CarStateData(
  oilTempCelsius: Short, // [ UNITS = Celsius ]
  oilPressureKPa: Int, // [ UNITS = Kilopascal ]
  waterTempCelsius: Short, // [ UNITS = Celsius ]
  waterPressureKpa: Int, // [ UNITS = Kilopascal ]
  fuelPressureKpa: Short, // [ UNITS = Kilopascal ]
  carFlags: Int,
  fuelCapacity: Int, // [ UNITS = Liters ]
  brake: Float,
  throttle: Float,
  clutch: Float,
  steering: Float,
  fuelLevel: Float,
  speed: Float,
  rpm: Int,
  maxRpm: Int,
  gear: Int, // [ RANGE = -1 (Reverse)  0 (Neutral)  1 (Gear 1)  2 (Gear 2)  etc... ]  TODO *Revers = 15?
  numGears: Int, // [ UNSET = -1 ]
  boostAmount: Int,
  enforcedPitStopLap: Byte,
  odometerKM: Float, // [ UNSET = -1.0f ]
  antiLockActive: Boolean,
  boostActive: Boolean) {
  def toJsonString: String = this.toJson.toString
}

case class CarStateVecotrData(
  orientation: Float, // [ UNITS = Euler Angles ]
  localVelocity: Float, // [ UNITS = Metres per-second ]
  worldVelocity: Float, // [ UNITS = Metres per-second ]
  angularVelocity: Float, // [ UNITS = Radians per-second ]
  localAcceleration: Float, // [ UNITS = Metres per-second ]
  worldAcceleration: Float, // [ UNITS = Metres per-second ]
  extentsCentre: Float // [ UNITS = Local Space  X  Y  Z ]
) {
  def toJsonString: String = this.toJson.toString
}

case class TyresData(
  terrain: Int, // 4
  tyreY: Float, // [ UNITS = Local Space  Y ]
  tyreRPS: Float, // [ UNITS = Revolutions per second ]
  tyreSlipSpeed: Float, // [ UNITS = Metres per-second ]
  tyreTemp: Float, // [ UNITS = Celsius ]
  tyreGrip: Float,
  tyreHeightAboveGround: Float, // [ UNITS = Local Space  Y ]
  tyreLateralStiffness: Float, // [ UNITS = Lateral stiffness coefficient used in tyre deformation ]
  tyreWear: Float,
  brakeDamage: Float,
  suspensionDamage: Float,
  brakeTempCelsius: Float, // [ UNITS = Celsius ]
  tyreTreadTemp: Float, // [ UNITS = Kelvin ]
  tyreLayerTemp: Float, // [ UNITS = Kelvin ]
  tyreCarcassTemp: Float, // [ UNITS = Kelvin ]
  tyreRimTemp: Float, // [ UNITS = Kelvin ]
  tyreInternalAirTemp: Float, // [ UNITS = Kelvin ]
  wheelLocalPositionY: Float,
  rideHeight: Float,
  suspensionTravel: Float,
  suspensionVelocity: Float,
  airPressure: Float) {
  def toJsonString: String = this.toJson.toString
}

case class ExtraData(
  engineSpeed: Float,
  engineTorque: Float) {
  def toJsonString: String = this.toJson.toString
}

case class CarDamageData(
  crashState: Int,
  aeroDamage: Float,
  engineDamage: Float) {
  def toJsonString: String = this.toJson.toString
}

case class WeatherData(
  ambientTemperature: Byte, // [ UNITS = Celsius ] [ UNSET = 25.0f ]
  trackTemperature: Byte, // [ UNITS = Celsius ] [ UNSET = 30.0f ]
  rainDensity: Float, // [ UNITS = How much rain will fall ]
  windSpeed: Int,
  windDirectionX: Float, // [ UNITS = Normalised Vector X ]
  windDirectionY: Float // [ UNITS = Normalised Vector Y ]
) {
  def toJsonString: String = this.toJson.toString
}

case class TelemetryData(
  frameType: Int = TelemetryDataConst.TELEMETRY_DATA_FRAME_TYPE,
  gameStateData: GameStateData,
  participantInfoData: ParticipantInfoData,
  participantInfo: Array[ParticipantInfo],
  unfilteredInputData: UnfilteredInputData,
  eventInfoData: EventInfoData,
  timingInfoData: TimingInfoData,
  sectorTimeData: SectorTimeData,
  flagData: FlagData,
  pitInfoData: PitInfoData,
  carStateData: CarStateData,
  carStateVecotrDataX: CarStateVecotrData,
  carStateVecotrDataY: CarStateVecotrData,
  carStateVecotrDataZ: CarStateVecotrData,
  tyresDataFrontLeft: TyresData,
  tyresDataFrontRight: TyresData,
  tyresDataRearLeft: TyresData,
  tyresDataRearRight: TyresData,
  extraData: ExtraData,
  carDamageData: CarDamageData,
  weatherData: WeatherData) {
  def toJsonString: String = this.toJson.toString
}
