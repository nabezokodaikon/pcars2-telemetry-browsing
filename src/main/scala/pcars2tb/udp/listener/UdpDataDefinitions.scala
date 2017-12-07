package pcars2tb.udp.listener

import spray.json._
import DefaultJsonProtocol._

object UdpDataJsonProtocol extends DefaultJsonProtocol {
  implicit val packetBaseFormat = jsonFormat8(PacketBase)
  implicit val telemetryParticipantInfoFormat = jsonFormat1(TelemetryParticipantInfo)
  implicit val unfilteredInputFormat = jsonFormat4(UnfilteredInput)
  implicit val carFlagsFormat = jsonFormat6(CarFlags)
  implicit val carStateFormat = jsonFormat21(CarState)
  implicit val velocityFormat = jsonFormat7(Velocity)
  implicit val tyreFlagsFormat = jsonFormat3(TyreFlags)
  implicit val tyre1Format = jsonFormat22(Tyre1)
  implicit val tyre2Format = jsonFormat2(Tyre2)
  implicit val tyre3Format = jsonFormat5(Tyre3)
  implicit val carDamageFormat = jsonFormat2(CarDamage)
  implicit val hwStateFormat = jsonFormat6(HWState)
  implicit val telemetryDataFormat = jsonFormat10(TelemetryData)
  implicit val raceDataFormat = jsonFormat19(RaceData)
  implicit val participantsDataFormat = jsonFormat5(ParticipantsData)
  implicit val participantInfoFormat = jsonFormat17(ParticipantInfo)
  implicit val formatParticipantInfoFormat = jsonFormat22(FormatParticipantInfo)
  implicit val timingsDataFormat = jsonFormat10(TimingsData)
  implicit val gameStateFormat = jsonFormat2(GameState)
  implicit val gameSessionStateFormat = jsonFormat2(SessionState)
  implicit val gameStateDataFormat = jsonFormat11(GameStateData)
  implicit val participantStatsInfoFormat = jsonFormat8(ParticipantStatsInfo)
  implicit val formatParticipantStatsInfoFormat = jsonFormat8(FormatParticipantStatsInfo)
  implicit val participantsStatsFormat = jsonFormat2(ParticipantsStats)
  implicit val timeStatsDataFormat = jsonFormat3(TimeStatsData)
  implicit val vehicleInfoFormat = jsonFormat3(VehicleInfo)
  implicit val participantVehicleNamesDataFormat = jsonFormat2(ParticipantVehicleNamesData)
  implicit val classInfoFormat = jsonFormat2(ClassInfo)
  implicit val vehicleClassNamesDataFormat = jsonFormat2(VehicleClassNamesData)

  implicit val lapTimeFormat = jsonFormat6(LapTime)
  implicit val lapTimeDetailsFormat = jsonFormat7(LapTimeDetails)
  implicit val aggregateTimeFormat = jsonFormat3(AggregateTime)
  implicit val fuelDataFormat = jsonFormat3(FuelData)
}

import UdpDataJsonProtocol._

object UdpStreamerPacketHandlerType {
  val CAR_PHYSICS: Byte = 0 // TelemetryData
  val RACE_DEFINITION: Byte = 1 // RaceData
  val PARTICIPANTS: Byte = 2 // ParticipantsData
  val TIMINGS: Byte = 3 // TimingsData
  val GAME_STATE: Byte = 4 // GameStateData
  val WEATHER_STATE: Byte = 5 // not sent at the moment, information can be found in the game state packet
  val VEHICLE_NAMES: Byte = 6 // not sent at the moment
  val TIME_STATS: Byte = 7 // TimeStatsData
  val PARTICIPANT_VEHICLE_NAMES: Byte = 8 // ParticipantVehicleNamesData or VehicleClassNamesData

  /*
   * Original data
   */
  val LAP_TIME_DETAILS: Byte = 64
  val AGGREGATE_TIME: Byte = 65
  val FUEL_DATA: Byte = 66
}

object PacketSize {
  val TELEMETRY_DATA: Short = 555
  val RACE_DATA: Short = 308
  val PARTICIPANTS_DATA: Short = 1136
  val TIMINGS_DATA: Short = 1059
  val GAME_STATE_DATA: Short = 24
  val TIME_STATS_DATA: Short = 1040
  val PARTICIPANT_VEHICLE_NAMES_DATA: Short = 1164
  val VEHICLE_CLASS_NAMES_DATA: Short = 1452
}

object UdpDataConst {
  val UDP_STREAMER_PARTICIPANTS_SUPPORTED: Byte = 32
  val UDP_STREAMER_CAR_PHYSICS_HANDLER_VERSION: Byte = 2
  val TYRE_NAME_LENGTH_MAX: Byte = 40
  val TRACKNAME_LENGTH_MAX: Byte = 64
  val PARTICIPANTS_PER_PACKET: Byte = 16
  val PARTICIPANT_NAME_LENGTH_MAX: Byte = 64
  val VEHICLE_NAME_LENGTH_MAX: Byte = 64
  val VEHICLES_PER_PACKET: Byte = 16
  val CLASS_NAME_LENGTH_MAX: Byte = 20
  val CLASSES_SUPPORTED_PER_PACKET: Byte = 60

  val GEAR_NEUTRAL: String = "N"
  val GEAR_REVERS: String = "R"
}

trait UdpData {
  val base: PacketBase
  def toJsonString(): String
}

/*
 * Each packet holds a base header with identification info to help with UDP unreliability.
 */
case class PacketBase(
    packetNumber: Long, // 0 counter reflecting all the packets that have been sent during the game run
    categoryPacketNumber: Long, // 4 counter of the packet groups belonging to the given category
    partialPacketIndex: Short, // 8 If the data from this class had to be sent in several packets, the index number
    partialPacketNumber: Short, // 9 If the data from this class had to be sent in several packets, the total number
    packetType: Short, // 10 what is the type of this packet (see EUDPStreamerPacketHanlderType for details)
    packetVersion: Short, // 11 what is the version of protocol for this handler, to be bumped with data structure change
    dataTimestamp: Long,
    dataSize: Short
)

/*******************************************************************************************************************
//
//	Telemetry data for the viewed participant. 
//
//	Frequency: Each tick of the UDP streamer how it is set in the options
//	When it is sent: in race
//
*******************************************************************************************************************/
case class TelemetryParticipantInfo(
    viewedParticipantIndex: Byte
)

case class UnfilteredInput(
    unfilteredThrottle: Short, // 0 - 255
    unfilteredBrake: Short, // 0 - 255
    unfilteredSteering: Byte, // -127(Left) - +127(Right)
    unfilteredClutch: Short // 0 - 255
)

object CarFlagsDefine {
  val CAR_HEADLIGHT: Byte = (1 << 0).toByte
  val CAR_ENGINE_ACTIVE: Byte = (1 << 1).toByte
  val CAR_ENGINE_WARNING: Byte = (1 << 2).toByte
  val CAR_SPEED_LIMITER: Byte = (1 << 3).toByte
  val CAR_ABS: Byte = (1 << 4).toByte
  val CAR_HANDBRAKE: Byte = (1 << 5).toByte
}

case class CarFlags(
    headLight: Boolean,
    engineActive: Boolean,
    engineWarning: Boolean,
    speedLimiter: Boolean,
    abs: Boolean,
    handbrake: Boolean
) {
  def toJsonString: String = this.toJson.toString
}

case class CarState(
    carFlags: CarFlags,
    oilTempCelsius: String, // [ Unit: Celsius ] [ value / 255f ]
    oilPressureKPa: Int,
    waterTempCelsius: String, // [ Unit: Celsius ] [ value / 255f ]
    waterPressureKpa: Int,
    fuelPressureKpa: Int,
    fuelCapacity: Short, // [ Unit: liter ]
    brake: Short, // [ 0 - 255 ]
    throttle: Short, // [ 0 - 255 ]
    clutch: Short, // [ 0 - 255 ]
    fuelLevel: Float,
    fuelRemaining: String,
    speed: Float, // [ Unit: KM/H ] [ value * 3.6f ]
    rpm: Int,
    maxRpm: Int,
    steering: Byte, // [ -127(Left) - +127(Right) ]
    gear: String,
    numGears: Byte,
    boostAmount: Short,
    crashState: Short,
    odometerKM: Float
)

case class Velocity(
    orientation: Array[Float],
    localVelocity: Array[Float],
    worldVelocity: Array[Float],
    angularVelocity: Array[Float],
    localAcceleration: Array[Float],
    worldAcceleration: Array[Float],
    extentsCentre: Array[Float]
)

object TyreFlagsDefine {
  val TYRE_ATTACHED: Byte = (1 << 0)
  val TYRE_INFLATED: Byte = (1 << 1)
  val TYRE_IS_ON_GROUND: Byte = (1 << 2)
}

case class TyreFlags(
    attached: Boolean,
    inflated: Boolean,
    isOnGround: Boolean
) {
  def toJsonString: String = this.toJson.toString
}

case class Tyre1(
    tyreFlags: Array[TyreFlags],
    terrain: Array[Short],
    tyreY: Array[Float],
    tyreRPS: Array[Float],
    tyreTemp: Array[Short],
    tyreHeightAboveGround: Array[Float],
    tyreWear: Array[Short],
    brakeDamage: Array[String],
    suspensionDamage: Array[String],
    brakeTempCelsius: Array[Short], // [ Unit: Celsius ] [ Convert: value / 255 ]
    tyreTreadTemp: Array[Int],
    tyreLayerTemp: Array[Int],
    tyreCarcassTemp: Array[Int],
    tyreRimTemp: Array[Int],
    tyreInternalAirTemp: Array[Int],
    tyreTempLeft: Array[Int], // [ Unit: Celsius ]
    tyreTempCenter: Array[Int], // [ Unit: Celsius ]
    tyreTempRight: Array[Int], // [ Unit: Celsius ]
    wheelLocalPositionY: Array[Float],
    rideHeight: Array[Float], // [ Unit: Metric ]
    suspensionTravel: Array[Float], // [ Unit: Metric ]
    suspensionVelocity: Array[Float]
)
case class Tyre2(
    suspensionRideHeight: Array[Int],
    airPressure: Array[Int] // [ Unit: bar ] [ Convert: value / 10 ]
)

case class Tyre3(
    engineSpeed: String,
    engineTorque: String,
    enginePower: String,
    wings: Array[Short],
    handBrake: Short
)

case class CarDamage(
    aeroDamage: String,
    engineDamage: String
)

case class HWState(
    joyPad0: Long,
    dPad: Short,
    tyreCompound: Array[String],
    turboBoostPressure: Float,
    fullPosition: Array[Float], // position of the viewed participant with full precision
    brakeBias: Short // quantized brake bias
)

// partialPacketNumber = 1 Only
case class TelemetryData(
    base: PacketBase,
    participantInfo: TelemetryParticipantInfo,
    unfilteredInput: UnfilteredInput,
    carState: CarState,
    velocity: Velocity,
    tyre1: Tyre1,
    tyre2: Tyre2,
    tyre3: Tyre3,
    carDamage: CarDamage,
    hwState: HWState
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Race stats data.  
//
//	Frequency: Logaritmic decrease
//	When it is sent: Counter resets on entering InRace state and again each time any of the values changes
//
*******************************************************************************************************************/
// partialPacketNumber = 1 Only
case class RaceData(
    base: PacketBase,
    worldFastestLapTime: String, // [ Unit: Seconds ]
    personalFastestLapTime: String, // [ Unit: Seconds ]
    personalFastestSector1Time: String, // [ Unit: Seconds ]
    personalFastestSector2Time: String, // [ Unit: Seconds ]
    personalFastestSector3Time: String, // [ Unit: Seconds ]
    worldFastestSector1Time: String, // [ Unit: Seconds ]
    worldFastestSector2Time: String, // [ Unit: Seconds ]
    worldFastestSector3Time: String, // [ Unit: Seconds ]
    trackLength: Float,
    trackLocation: String,
    trackVariation: String,
    translatedTrackLocation: String,
    translatedTrackVariation: String,
    lapsTimeInEvent: Int, // contains lap number for lap based session or quantized session duration (number of 5mins) for timed sessions, the top bit is 1 for timed sessions TODO: セッション時間の場合、常に値が 65535 になる。
    isTimedSessions: Boolean,
    lapsInEvent: Int,
    sessionLengthTimeInEvent: Int,
    enforcedPitStopLap: Byte
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Participant names data.  
//
//	Frequency: Logarithmic decrease
//	When it is sent: Counter resets on entering InRace state and again each  the participants change. 
//	The sParticipantsChangedTimestamp represent last time the participants has changed andis  to be used to sync 
//	this information with the rest of the participant related packets
//
*******************************************************************************************************************/
// partialPacketNumber = 1 or 2
case class ParticipantsData(
    base: PacketBase,
    participantsChangedTimestamp: Long,
    name: Array[String],
    nationality: Array[Long],
    index: Array[Int] // session unique index for MP races
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Participant timings data.  
//
//	Frequency: Each tick of the UDP streamer how it is set in the options.
//	When it is sent: in race
//
*******************************************************************************************************************/
case class RaceState(value: Byte, text: String)

object RaceStateDefine {
  val RACESTATE_INVALID: Byte = 0
  val RACESTATE_NOT_STARTED: Byte = 1
  val RACESTATE_RACING: Byte = 2
  val RACESTATE_FINISHED: Byte = 3
  val RACESTATE_DISQUALIFIED: Byte = 4
  val RACESTATE_RETIRED: Byte = 5
  val RACESTATE_DNF: Byte = 6
  val RACESTATE_UNKNOWN: Byte = 127
}

object RaceStateDefineValue {
  val RACESTATE_INVALID: RaceState = RaceState(RaceStateDefine.RACESTATE_INVALID, "RACESTATE_INVALID")
  val RACESTATE_NOT_STARTED: RaceState = RaceState(RaceStateDefine.RACESTATE_NOT_STARTED, "RACESTATE_NOT_STARTED")
  val RACESTATE_RACING: RaceState = RaceState(RaceStateDefine.RACESTATE_RACING, "RACESTATE_RACING")
  val RACESTATE_FINISHED: RaceState = RaceState(RaceStateDefine.RACESTATE_FINISHED, "RACESTATE_FINISHED")
  val RACESTATE_DISQUALIFIED: RaceState = RaceState(RaceStateDefine.RACESTATE_DISQUALIFIED, "RACESTATE_DISQUALIFIED")
  val RACESTATE_RETIRED: RaceState = RaceState(RaceStateDefine.RACESTATE_RETIRED, "RACESTATE_RETIRED")
  val RACESTATE_DNF: RaceState = RaceState(RaceStateDefine.RACESTATE_DNF, "RACESTATE_DNF")
  val RACESTATE_UNKNOWN: RaceState = RaceState(RaceStateDefine.RACESTATE_UNKNOWN, "RACESTATE_UNKNOWN")
}

case class PitMode(value: Byte, text: String)

object PitModeDefine {
  val PIT_MODE_NONE: Byte = 0
  val PIT_MODE_DRIVING_INTO_PITS: Byte = 1
  val PIT_MODE_IN_PIT: Byte = 2
  val PIT_MODE_DRIVING_OUT_OF_PITS: Byte = 3
  val PIT_MODE_IN_GARAGE: Byte = 4
  val PIT_MODE_DRIVING_OUT_OF_GARAGE: Byte = 5
  val PIT_MODE_UNKNOWN: Byte = 127
}

object PitModeDefineValue {
  val PIT_MODE_NONE: PitMode = PitMode(PitModeDefine.PIT_MODE_NONE, "PIT_MODE_NONE")
  val PIT_MODE_DRIVING_INTO_PITS: PitMode = PitMode(PitModeDefine.PIT_MODE_DRIVING_INTO_PITS, "PIT_MODE_DRIVING_INTO_PITS")
  val PIT_MODE_IN_PIT: PitMode = PitMode(PitModeDefine.PIT_MODE_IN_PIT, "PIT_MODE_IN_PIT")
  val PIT_MODE_DRIVING_OUT_OF_PITS: PitMode = PitMode(PitModeDefine.PIT_MODE_DRIVING_OUT_OF_PITS, "PIT_MODE_DRIVING_OUT_OF_PITS")
  val PIT_MODE_IN_GARAGE: PitMode = PitMode(PitModeDefine.PIT_MODE_IN_GARAGE, "PIT_MODE_IN_GARAGE")
  val PIT_MODE_DRIVING_OUT_OF_GARAGE: PitMode = PitMode(PitModeDefine.PIT_MODE_DRIVING_OUT_OF_GARAGE, "PIT_MODE_DRIVING_OUT_OF_GARAGE")
  val PIT_MODE_UNKNOWN: PitMode = PitMode(PitModeDefine.PIT_MODE_UNKNOWN, "PIT_MODE_UNKNOWN")
}

case class PitSchedule(value: Byte, text: String)

object PitScheduleDefine {
  val PIT_SCHEDULE_NONE: Byte = 0 // Nothing scheduled
  val PIT_SCHEDULE_PLAYER_REQUESTED: Byte = 1 // Used for standard pit sequence - requested by player
  val PIT_SCHEDULE_ENGINEER_REQUESTED: Byte = 2 // Used for standard pit sequence - requested by engineer
  val PIT_SCHEDULE_DAMAGE_REQUESTED: Byte = 3 // Used for standard pit sequence - requested by engineer for damage
  val PIT_SCHEDULE_MANDATORY: Byte = 4 // Used for standard pit sequence - requested by engineer from career enforced lap number
  val PIT_SCHEDULE_DRIVE_THROUGH: Byte = 5 // Used for drive-through penalty
  val PIT_SCHEDULE_STOP_GO: Byte = 6 // Used for stop-go penalty
  val PIT_SCHEDULE_PITSPOT_OCCUPIED: Byte = 7 // Used for drive-through when pitspot is occupied
  val PIT_SCHEDULE_UNKNOWN: Byte = 127
}

object PitScheduleDefineValue {
  val PIT_SCHEDULE_NONE: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_NONE, "PIT_SCHEDULE_NONE")
  val PIT_SCHEDULE_PLAYER_REQUESTED: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_PLAYER_REQUESTED, "PIT_SCHEDULE_PLAYER_REQUESTED")
  val PIT_SCHEDULE_ENGINEER_REQUESTED: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_ENGINEER_REQUESTED, "PIT_SCHEDULE_ENGINEER_REQUESTED")
  val PIT_SCHEDULE_DAMAGE_REQUESTED: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_DAMAGE_REQUESTED, "PIT_SCHEDULE_DAMAGE_REQUESTED")
  val PIT_SCHEDULE_MANDATORY: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_MANDATORY, "PIT_SCHEDULE_MANDATORY")
  val PIT_SCHEDULE_DRIVE_THROUGH: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_DRIVE_THROUGH, "PIT_SCHEDULE_DRIVE_THROUGH")
  val PIT_SCHEDULE_STOP_GO: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_STOP_GO, "PIT_SCHEDULE_STOP_GO")
  val PIT_SCHEDULE_PITSPOT_OCCUPIED: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_PITSPOT_OCCUPIED, "PIT_SCHEDULE_PITSPOT_OCCUPIED")
  val PIT_SCHEDULE_UNKNOWN: PitSchedule = PitSchedule(PitScheduleDefine.PIT_SCHEDULE_UNKNOWN, "PIT_SCHEDULE_UNKNOWN")
}

case class FlagColor(value: Byte, text: String)

object FlagColorDefine {
  val FLAG_COLOUR_NONE: Byte = 0 // Not used for actual flags, only for some query functions
  val FLAG_COLOUR_GREEN: Byte = 1 // End of danger zone, or race started
  val FLAG_COLOUR_BLUE: Byte = 2 // Faster car wants to overtake the participant
  val FLAG_COLOUR_WHITE_SLOW_CAR: Byte = 3 // Slow car in area
  val FLAG_COLOUR_WHITE_FINAL_LAP: Byte = 4 // Final Lap
  val FLAG_COLOUR_RED: Byte = 5 // Huge collisions where one or more cars become wrecked and block the track
  val FLAG_COLOUR_YELLOW: Byte = 6 // Danger on the racing surface itself
  val FLAG_COLOUR_DOUBLE_YELLOW: Byte = 7 // Danger that wholly or partly blocks the racing surface
  val FLAG_COLOUR_BLACK_AND_WHITE: Byte = 8 // Unsportsmanlike conduct
  val FLAG_COLOUR_BLACK_ORANGE_CIRCLE: Byte = 9 // Mechanical Failure
  val FLAG_COLOUR_BLACK: Byte = 10 // Participant disqualified
  val FLAG_COLOUR_CHEQUERED: Byte = 11 // Chequered flag
  val FLAG_COLOUR_UNKNOWN: Byte = 127
}

object FlagColorDefineValue {
  val FLAG_COLOUR_NONE: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_NONE, "FLAG_COLOUR_NONE")
  val FLAG_COLOUR_GREEN: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_GREEN, "FLAG_COLOUR_GREEN")
  val FLAG_COLOUR_BLUE: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_BLUE, "FLAG_COLOUR_BLUE")
  val FLAG_COLOUR_WHITE_SLOW_CAR: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_WHITE_SLOW_CAR, "FLAG_COLOUR_WHITE_SLOW_CAR")
  val FLAG_COLOUR_WHITE_FINAL_LAP: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_WHITE_FINAL_LAP, "FLAG_COLOUR_WHITE_FINAL_LAP")
  val FLAG_COLOUR_RED: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_RED, "FLAG_COLOUR_RED")
  val FLAG_COLOUR_YELLOW: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_YELLOW, "FLAG_COLOUR_YELLOW")
  val FLAG_COLOUR_DOUBLE_YELLOW: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_DOUBLE_YELLOW, "FLAG_COLOUR_DOUBLE_YELLOW")
  val FLAG_COLOUR_BLACK_AND_WHITE: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_BLACK_AND_WHITE, "FLAG_COLOUR_BLACK_AND_WHITE")
  val FLAG_COLOUR_BLACK_ORANGE_CIRCLE: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_BLACK_ORANGE_CIRCLE, "FLAG_COLOUR_BLACK_ORANGE_CIRCLE")
  val FLAG_COLOUR_BLACK: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_BLACK, "FLAG_COLOUR_BLACK")
  val FLAG_COLOUR_CHEQUERED: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_CHEQUERED, "FLAG_COLOUR_CHEQUERED")
  val FLAG_COLOUR_UNKNOWN: FlagColor = FlagColor(FlagColorDefine.FLAG_COLOUR_UNKNOWN, "FLAG_COLOUR_UNKNOWN")
}

case class FlagReason(value: Byte, text: String)

object FlagReasonDefine {
  val FLAG_REASON_NONE: Byte = 0
  val FLAG_REASON_SOLO_CRASH: Byte = 1
  val FLAG_REASON_VEHICLE_CRASH: Byte = 2
  val FLAG_REASON_VEHICLE_OBSTRUCTION: Byte = 3
  val FLAG_REASON_UNKNOWN: Byte = 127
}

object FlagReasonDefineValue {
  val FLAG_REASON_NONE: FlagReason = FlagReason(FlagReasonDefine.FLAG_REASON_NONE, "FLAG_REASON_NONE")
  val FLAG_REASON_SOLO_CRASH: FlagReason = FlagReason(FlagReasonDefine.FLAG_REASON_SOLO_CRASH, "FLAG_REASON_SOLO_CRASH")
  val FLAG_REASON_VEHICLE_CRASH: FlagReason = FlagReason(FlagReasonDefine.FLAG_REASON_VEHICLE_CRASH, "FLAG_REASON_VEHICLE_CRASH")
  val FLAG_REASON_VEHICLE_OBSTRUCTION: FlagReason = FlagReason(FlagReasonDefine.FLAG_REASON_VEHICLE_OBSTRUCTION, "FLAG_REASON_VEHICLE_OBSTRUCTION")
  val FLAG_REASON_UNKNOWN: FlagReason = FlagReason(FlagReasonDefine.FLAG_REASON_UNKNOWN, "FLAG_REASON_UNKNOWN")
}

case class FormatParticipantInfo(
    worldPosition: Array[Short],
    orientation: Array[Short], // Quantized heading (-PI .. +PI) , Quantized pitch (-PI / 2 .. +PI / 2),  Quantized bank (-PI .. +PI).
    currentLapDistance: Int,
    racePosition: Short, // holds the race position, + top bit shows if the participant is active or not
    isActive: Boolean,
    sector: Short, // sector + extra precision bits for x/z position
    flagColor: Byte,
    flagColorString: String,
    flagReason: Byte,
    flagReasonString: String,
    pitMode: Byte,
    pitModeString: String,
    pitSchedule: Byte,
    pitScheduleString: String,
    carIndex: Int, // top bit shows if participant is (local or remote) human player or not
    raceState: Byte, // race state flags + invalidated lap indication --
    raceStateString: String,
    lapInvalidated: Boolean,
    currentLap: Short,
    currentTime: String, // [ Unit: Seconds ]
    currentSectorTime: String, // [ Unit: Seconds ]
    mpParticipantIndex: Int // matching sIndex from sParticipantsData
)

case class ParticipantInfo(
    worldPosition: Array[Short],
    orientation: Array[Short], // Quantized heading (-PI .. +PI) , Quantized pitch (-PI / 2 .. +PI / 2),  Quantized bank (-PI .. +PI).
    currentLapDistance: Int,
    racePosition: Short, // holds the race position, + top bit shows if the participant is active or not
    isActive: Boolean,
    sector: Short, // sector + extra precision bits for x/z position
    flagColor: Byte,
    flagReason: Byte,
    pitMode: Byte,
    pitSchedule: Byte,
    carIndex: Int, // top bit shows if participant is (local or remote) human player or not
    raceState: Byte, // race state flags + invalidated lap indication --
    lapInvalidated: Boolean,
    currentLap: Short,
    currentTime: Float, // [ Unit: Seconds ]
    currentSectorTime: Float, // [ Unit: Seconds ]
    mpParticipantIndex: Int // matching sIndex from sParticipantsData
)

// partialPacketNumber = 1 Only
case class TimingsData(
    base: PacketBase,
    numParticipants: Byte,
    participantsChangedTimestamp: Long,
    eventTimeRemaining: String, // [ Unit: Seconds ] time remaining, -1 for invalid time,  -1 - laps remaining in lap based races  --
    splitTimeAhead: Float,
    splitTimeBehind: Float,
    splitTime: Float,
    participants: Array[ParticipantInfo],
    formatParticipants: Array[FormatParticipantInfo],
    localParticipantIndex: Int // identifies which of the MP participants is the local player
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Game State. 
//
//	Frequency: Each 5s while being in Main Menu, Each 10s while being in race + on each change Main Menu<->Race several times.
//	the frequency in Race is increased in case of weather timer being faster  up to each 5s for 30x time progression
//	When it is sent: Always
//
*******************************************************************************************************************/
object GameStateDefine {
  val GAME_EXITED: Byte = 0
  val GAME_FRONT_END: Byte = 1
  val GAME_INGAME_PLAYING: Byte = 2
  val GAME_INGAME_PAUSED: Byte = 3
  val GAME_INGAME_INMENU_TIME_TICKING: Byte = 4
  val GAME_INGAME_RESTARTING: Byte = 5
  val GAME_INGAME_REPLAY: Byte = 6
  val GAME_FRONT_END_REPLAY: Byte = 7
  val GAME_UNKNOWN: Byte = 127
}

object GameStateDefineValue {
  val GAME_EXITED = GameState(GameStateDefine.GAME_EXITED, "GAME_EXITED")
  val GAME_FRONT_END = GameState(GameStateDefine.GAME_FRONT_END, "GAME_FRONT_END")
  val GAME_INGAME_PLAYING = GameState(GameStateDefine.GAME_INGAME_PLAYING, "GAME_INGAME_PLAYING")
  val GAME_INGAME_PAUSED = GameState(GameStateDefine.GAME_INGAME_PAUSED, "GAME_INGAME_PAUSED")
  val GAME_INGAME_INMENU_TIME_TICKING = GameState(GameStateDefine.GAME_INGAME_INMENU_TIME_TICKING, "GAME_INGAME_INMENU_TIME_TICKING")
  val GAME_INGAME_RESTARTING = GameState(GameStateDefine.GAME_INGAME_RESTARTING, "GAME_INGAME_RESTARTING")
  val GAME_INGAME_REPLAY = GameState(GameStateDefine.GAME_INGAME_REPLAY, "GAME_INGAME_REPLAY")
  val GAME_FRONT_END_REPLAY = GameState(GameStateDefine.GAME_FRONT_END_REPLAY, "GAME_FRONT_END_REPLAY")
  val GAME_UNKNOWN = GameState(GameStateDefine.GAME_UNKNOWN, "Unknown")
}

object SessionStateDefine {
  val SESSION_INVALID: Byte = 0
  val SESSION_PRACTICE: Byte = 1
  val SESSION_TEST: Byte = 2
  val SESSION_QUALIFY: Byte = 3
  val SESSION_FORMATION_LAP: Byte = 4
  val SESSION_RACE: Byte = 5
  val SESSION_TIME_ATTACK: Byte = 6
  val SESSION_UNKNOWN: Byte = 127
}

object SessionStateDefineValue {
  val SESSION_INVALID = SessionState(SessionStateDefine.SESSION_INVALID, "SESSION_INVALID")
  val SESSION_PRACTICE = SessionState(SessionStateDefine.SESSION_PRACTICE, "SESSION_PRACTICE")
  val SESSION_TEST = SessionState(SessionStateDefine.SESSION_TEST, "SESSION_TEST")
  val SESSION_QUALIFY = SessionState(SessionStateDefine.SESSION_QUALIFY, "SESSION_QUALIFY")
  val SESSION_FORMATION_LAP = SessionState(SessionStateDefine.SESSION_FORMATION_LAP, "SESSION_FORMATION_LAP")
  val SESSION_RACE = SessionState(SessionStateDefine.SESSION_RACE, "SESSION_RACE")
  val SESSION_TIME_ATTACK = SessionState(SessionStateDefine.SESSION_TIME_ATTACK, "SESSION_TIME_ATTACK")
  val SESSION_UNKNOWN = SessionState(SessionStateDefine.SESSION_UNKNOWN, "Unknown")
}

case class GameState(value: Byte, text: String)
case class SessionState(value: Byte, text: String)

// partialPacketNumber = 1 Only
case class GameStateData(
    base: PacketBase,
    buildVersionNumber: Int,
    gameState: GameState, // first 3 bits are used for game state enum, second 3 bits for session state enum See shared memory example file for the enums
    sessionState: SessionState,
    ambientTemperature: Byte,
    trackTemperature: Byte,
    rainDensity: Short,
    snowDensity: Short,
    windSpeed: Byte,
    windDirectionX: Byte,
    windDirectionY: Byte // 22 padded to 24
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Participant Stats and records
//
//	Frequency: When entering the race and each time any of the values change, so basically each time any of the participants
//						crosses a sector boundary.
//	When it is sent: In Race
//
*******************************************************************************************************************/
case class FormatParticipantStatsInfo(
    fastestLapTime: String, // [ Unit: Seconds ]
    lastLapTime: String, // [ Unit: Seconds ]
    lastSectorTime: String, // [ Unit: Seconds ]
    fastestSector1Time: String, // [ Unit: Seconds ]
    fastestSector2Time: String, // [ Unit: Seconds ]
    fastestSector3Time: String, // [ Unit: Seconds ]
    participantOnlineRep: Long, // u16 rank type + u16 strength, 0 in SP races
    mpParticipantIndex: Int // matching sIndex from sParticipantsData
)

case class ParticipantStatsInfo(
    fastestLapTime: Float, // [ Unit: Seconds ]
    lastLapTime: Float, // [ Unit: Seconds ]
    lastSectorTime: Float, // [ Unit: Seconds ]
    fastestSector1Time: Float, // [ Unit: Seconds ]
    fastestSector2Time: Float, // [ Unit: Seconds ]
    fastestSector3Time: Float, // [ Unit: Seconds ]
    participantOnlineRep: Long, // u16 rank type + u16 strength, 0 in SP races
    mpParticipantIndex: Int // matching sIndex from sParticipantsData
)

case class ParticipantsStats(
    participants: Array[ParticipantStatsInfo],
    formatParticipants: Array[FormatParticipantStatsInfo]
)

// partialPacketNumber = 1 Only
case class TimeStatsData(
    base: PacketBase,
    participantsChangedTimestamp: Long,
    stats: ParticipantsStats
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Participant Vehicle names
//
//	Frequency: Logarithmic decrease
//	When it is sent: Counter resets on entering InRace state and again each  the participants change. 
//	The sParticipantsChangedTimestamp represent last time the participants has changed and is  to be used to sync 
//	this information with the rest of the participant related packets
//
//	Note: This data is always sent with at least 2 packets. The 1-(n-1) holds the vehicle name for each participant
//	The last one holding the class names.
//
*******************************************************************************************************************/
case class VehicleInfo(
    index: Int,
    carClass: Long,
    name: String
)

// partialPacketNumber = 1 or 2, 3
// partialPacketIndex From 1 to partialPacketNumber - 1
case class ParticipantVehicleNamesData(
    base: PacketBase,
    vehicles: Array[VehicleInfo]
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

case class ClassInfo(
    classIndex: Long,
    name: String
)

// partialPacketNumber = 1 , 2 or 3
// Measurement is impossible because partialPacketIndex and partialPacketNumber are always the same.
case class VehicleClassNamesData(
    base: PacketBase,
    classes: Array[ClassInfo]
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*
 * Time Data
 */
case class LapTime(
    lap: String,
    sector1: String,
    sector2: String,
    sector3: String,
    lapTime: String,
    delta: String
)

case class LapTimeDetails(
    base: PacketBase,
    isTimedSessions: Boolean,
    lapsInEvent: Int,
    current: LapTime,
    fastest: LapTime,
    average: LapTime,
    history: List[LapTime]
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*
 * Aggregate time
 */
case class AggregateTime(
    base: PacketBase,
    totalTime: String,
    gapTime: String
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}

/*
 * Fuel data
 */
case class FuelData(
    base: PacketBase,
    lastConsumption: String,
    averageConsumption: String
) extends UdpData {
  def toJsonString(): String = this.toJson.toString
}
