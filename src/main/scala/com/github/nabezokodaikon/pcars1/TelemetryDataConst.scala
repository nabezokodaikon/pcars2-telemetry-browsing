package com.github.nabezokodaikon.pcars1

object TelemetryDataConst {

  // Data size
  val TELEMETRY_DATA_SIZE: Int = 1367
  val PARTICIPANT_INFO_STRINGS_SIZE: Int = 1347
  val PARTICIPANT_INFO_STRINGS_ADDITIONAL_SIZE: Int = 1028

  // Frame type
  val TELEMETRY_DATA_FRAME_TYPE: Int = 0
  val PARTICIPANT_INFO_STRINGS_FRAME_TYPE: Int = 1
  val PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE: Int = 2

  // TyreData and TyreUdpData index
  val TYRE_FRONT_LEFT: Int = 0
  val TYRE_FRONT_RIGHT: Int = 1
  val TYRE_REAR_LEFT: Int = 2
  val TYRE_REAR_RIGHT: Int = 3

  // CarStateVecotrData index
  val VEC_X: Int = 0
  val VEC_Y: Int = 1
  val VEC_Z: Int = 2

  // Gear string
  val GEAR_NEUTRAL: String = "N"
  val GEAR_REVERS: String = "R"
}
