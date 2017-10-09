package com.github.nabezokodaikon.pcars1

case class FrameInfo(
  frameTypeAndSequence: Int,
  frameType: Int,
  sequence: Int)

// 1,347 Byte
case class ParticipantInfoStrings(
  buildVersionNumber: Int,
  packetType: Int,
  carName: String, // 64
  carClassName: String, // 64
  trackLocation: String, // 64
  trackVariation: String, // 64
  nameString: Array[String] // 16
) {
  val frameType = TelemetryDataConst.PARTICIPANT_INFO_STRINGS_FRAME_TYPE
}

// 1,028 Byte
case class ParticipantInfoStringsAdditional(
  buildVersionNumber: Int,
  packetType: Int,
  offset: Int,
  name: Array[String] // 16
) {
  val frameType = TelemetryDataConst.PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE
}

// 16 Byte
case class ParticipantInfo(
  worldPosition: Array[Short], // 3
  currentLapDistance: Int,
  racePosition: Int,
  lapsCompleted: Int,
  currentLap: Int,
  sector: Int,
  lastSectorTime: Float)

// 1,367 Byte
case class TelemetryData(
  buildVersionNumber: Int,
  packetType: Int,

  // Game states
  gameState: Int,
  sessionState: Int,
  raceStateFlags: Int,

  // Participant info
  viewedParticipantIndex: Byte,
  numParticipants: Byte,

  // Unfiltered input
  unfilteredThrottle: Float,
  unfilteredBrake: Float,
  unfilteredSteering: Float,
  unfilteredClutch: Float,

  // Event information
  lapsInEvent: Int,
  trackLength: Float,

  // Timings
  lapInvalidated: Boolean,
  bestLapTime: Float,
  lastLapTime: Float,
  currentTime: Float,
  splitTimeAhead: Float,
  splitTimeBehind: Float,
  splitTime: Float,
  eventTimeRemaining: Float,
  personalFastestLapTime: Float,
  worldFastestLapTime: Float,
  currentSector1Time: Float,
  currentSector2Time: Float,
  currentSector3Time: Float,
  fastestSector1Time: Float,
  fastestSector2Time: Float,
  fastestSector3Time: Float,
  personalFastestSector1Time: Float,
  personalFastestSector2Time: Float,
  personalFastestSector3Time: Float,
  worldFastestSector1Time: Float,
  worldFastestSector2Time: Float,
  worldFastestSector3Time: Float,
  joyPad1: Int,
  joyPad2: Int,

  // Flags
  highestFlagColor: Int,
  highestFlagReason: Int,

  // Pit info
  pitModeSchedule: Int,

  // Car state
  oilTempCelsius: Short,
  oilPressureKPa: Int,
  waterTempCelsius: Short,
  waterPressureKpa: Int,
  fuelPressureKpa: Short,
  carFlags: Int,
  fuelCapacity: Int,
  brake: Float,
  throttle: Float,
  clutch: Float,
  steering: Float,
  fuelLevel: Float,
  speed: Float,
  rpm: Int,
  maxRpm: Int,
  gearGears: Int, // Neutral = 0, Revers = 15
  gearNumGears: Int,
  boostAmount: Int,
  enforcedPitStopLap: Byte,
  crashState: Int,
  odometerKM: Float,
  orientation: Array[Float], // 3
  localVelocity: Array[Float], // 3
  worldVelocity: Array[Float], // 3
  angularVelocity: Array[Float], // 3
  localAcceleration: Array[Float], // 3
  worldAcceleration: Array[Float], // 3
  extentsCentre: Array[Float], // 3
  antiLockActive: Boolean,
  boostActive: Boolean,

  // Wheels / Tyres
  tyreFlags: Array[Int], // 4
  terrain: Array[Int], // 4
  tyreY: Array[Float], // 4
  tyreRPS: Array[Float], // 4
  tyreSlipSpeed: Array[Float], // 4
  tyreTemp: Array[Int], // 4
  tyreGrip: Array[Int], // 4
  tyreHeightAboveGround: Array[Float], // 4
  tyreLateralStiffness: Array[Float], // 4
  tyreWear: Array[Int], // 4
  brakeDamage: Array[Int], // 4
  suspensionDamage: Array[Int], // 4
  brakeTempCelsius: Array[Short], // 4
  tyreTreadTemp: Array[Int], // 4
  tyreLayerTemp: Array[Int], // 4
  tyreCarcassTemp: Array[Int], // 4
  tyreRimTemp: Array[Int], // 4
  tyreInternalAirTemp: Array[Int], // 4
  wheelLocalPositionY: Array[Float], // 4
  rideHeight: Array[Float], // 4
  suspensionTravel: Array[Float], // 4
  suspensionVelocity: Array[Float], // 4
  airPressure: Array[Int], // 4

  // Extras
  engineSpeed: Float,
  engineTorque: Float,

  // Car damage
  aeroDamage: Float,
  engineDamage: Float,

  // Weather
  ambientTemperature: Byte,
  trackTemperature: Byte,
  rainDensity: Float,
  windSpeed: Int,
  windDirectionX: Float,
  windDirectionY: Float,
  participantInfo: Array[ParticipantInfo], // 56
  wings: Array[Int], // 2
  dPad: Int) {
  val frameType = TelemetryDataConst.TELEMETRY_DATA_FRAME_TYPE
}
