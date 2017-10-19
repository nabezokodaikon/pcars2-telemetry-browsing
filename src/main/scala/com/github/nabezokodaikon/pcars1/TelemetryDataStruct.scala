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
  implicit val tyreDataFormat = jsonFormat18(TyreData)
  implicit val tyreUdpDataFormat = jsonFormat5(TyreUdpData)
  implicit val otherUdpDataFormat = jsonFormat2(OtherUdpData)
  implicit val carDamageDataFormat = jsonFormat3(CarDamageData)
  implicit val weatherDataFormat = jsonFormat6(WeatherData)
  implicit val telemetryDataFormat = jsonFormat17(TelemetryData)
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
  unfilteredThrottle: Float, // [ RANGE = 0.0f->1.0f ]
  unfilteredBrake: Float, // [ RANGE = 0.0f->1.0f ]
  unfilteredSteering: Float, // [ RANGE = -1.0f->1.0f ]
  unfilteredClutch: Float // [ RANGE = 0.0f->1.0f ]
) {
  def toJsonString: String = this.toJson.toString
}

case class EventInfoData(
  lapsInEvent: Int,
  trackLength: Float // [ UNITS = Kilometers ]
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
  oilTempCelsius: Float, // [ UNITS = Celsius ]
  oilPressureKPa: Int, // [ UNITS = Kilopascal ]
  waterTempCelsius: Float, // [ UNITS = Celsius ]
  waterPressureKpa: Int, // [ UNITS = Kilopascal ]
  fuelPressureKpa: Short, // [ UNITS = Kilopascal ]
  carFlags: Int,
  fuelCapacity: Int, // [ UNITS = Liters ]
  brake: Float,
  throttle: Float,
  clutch: Float,
  steering: Float,
  fuelLevel: Float, // [ UNITS = Liters ]
  speed: Float,
  rpm: Int,
  maxRpm: Int,
  gear: String, // [ RANGE = 15 (Reverse)  0 (Neutral)  1 (Gear 1)  2 (Gear 2)  etc... ]
  numGears: Int, // [ UNSET = -1 ]
  boostAmount: Int,
  enforcedPitStopLap: Byte,
  odometerKM: Float, // [ UNSET = -1.0f ]
  antiLockActive: Boolean,
  boostActive: Boolean) {
  def toJsonString: String = this.toJson.toString
}

case class CarStateVecotrData(
  orientation: Array[Float], // [ UNITS = Euler Angles ]
  localVelocity: Array[Float], // [ UNITS = Metres per-second ]
  worldVelocity: Array[Float], // [ UNITS = Metres per-second ]
  angularVelocity: Array[Float], // [ UNITS = Radians per-second ]
  localAcceleration: Array[Float], // [ UNITS = Metres per-second ]
  worldAcceleration: Array[Float], // [ UNITS = Metres per-second ]
  extentsCentre: Array[Float] // [ UNITS = Local Space  X  Y  Z ]
) {
  def toJsonString: String = this.toJson.toString
}

case class TyreData(
  tyreFlag: Array[Int],
  terrain: Array[Int],
  tyreY: Array[Float], // [ UNITS = Local Space  Y ]
  tyreRPS: Array[Float], // [ UNITS = Revolutions per second ]
  tyreSlipSpeed: Array[Float], // [ UNITS = Metres per-second ]
  tyreTemp: Array[Int], // [ UNITS = Celsius ]
  tyreGrip: Array[Int],
  tyreHeightAboveGround: Array[Float], // [ UNITS = Local Space  Y ]
  tyreLateralStiffness: Array[Float], // [ UNITS = Lateral stiffness coefficient used in tyre deformation ]
  tyreWear: Array[Int], // [ タイヤ摩耗 ]
  brakeDamage: Array[Int],
  suspensionDamage: Array[Int],
  brakeTempCelsius: Array[Float], // [ UNITS = Celsius ]
  tyreTreadTemp: Array[Float], // [ UNITS = Kelvin ]
  tyreLayerTemp: Array[Float], // [ UNITS = Kelvin ]
  tyreCarcassTemp: Array[Float], // [ UNITS = Kelvin ]
  tyreRimTemp: Array[Float], // [ UNITS = Kelvin ]
  tyreInternalAirTemp: Array[Float] // [ UNITS = Kelvin ]
) {
  def toJsonString: String = this.toJson.toString
}

case class TyreUdpData(
  wheelLocalPositionY: Array[Float],
  rideHeight: Array[Float], // [ UNITS = Centimeter ] タイヤサイドのHEIGHT
  suspensionTravel: Array[Float], // [ UNITS = Centimeter ] タイヤサイドのTRAVEL
  suspensionVelocity: Array[Float],
  airPressure: Array[Float] // [ UNITS = bar]
) {
  def toJsonString: String = this.toJson.toString
}

case class OtherUdpData(
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
  carStateVecotrData: CarStateVecotrData,
  tyreData: TyreData,
  tyreUdpData: TyreUdpData,
  otherUdpData: OtherUdpData,
  carDamageData: CarDamageData,
  weatherData: WeatherData) {
  def toJsonString: String = this.toJson.toString
}
