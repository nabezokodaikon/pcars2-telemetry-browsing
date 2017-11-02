package com.github.nabezokodaikon.pcars2

import spray.json._
import DefaultJsonProtocol._

object UdpDataJsonProtocol extends DefaultJsonProtocol {
  implicit val packetBaseFormat = jsonFormat6(PacketBase)
  implicit val telemetryParticipantInfoFormat = jsonFormat1(TelemetryParticipantInfo)
  implicit val unfilteredInputFormat = jsonFormat4(UnfilteredInput)
  implicit val carStateFormat = jsonFormat19(CarState)
  implicit val velocityFormat = jsonFormat7(Velocity)
  implicit val tyre1Format = jsonFormat22(Tyre1)
  implicit val tyre2Format = jsonFormat2(Tyre2)
  implicit val tyre3Format = jsonFormat4(Tyre3)
  implicit val carDamageFormat = jsonFormat2(CarDamage)
  implicit val hwStateFormat = jsonFormat3(HWState)
  implicit val telemetryDataFormat = jsonFormat12(TelemetryData)
  implicit val raceDataFormat = jsonFormat18(RaceData)
  implicit val participantsDataFormat = jsonFormat5(ParticipantsData)
  implicit val participantInfoFormat = jsonFormat12(ParticipantInfo)
  implicit val timingsDataFormat = jsonFormat10(TimingsData)
  implicit val gameStateDataFormat = jsonFormat13(GameStateData)
  implicit val participantStatsInfoFormat = jsonFormat6(ParticipantStatsInfo)
  implicit val participantsStatsFormat = jsonFormat1(ParticipantsStats)
  implicit val timeStatsDataFormat = jsonFormat5(TimeStatsData)
  implicit val vehicleInfoFormat = jsonFormat3(VehicleInfo)
  implicit val participantVehicleNamesDataFormat = jsonFormat4(ParticipantVehicleNamesData)
  implicit val classInfoFormat = jsonFormat2(ClassInfo)
  implicit val vehicleClassNamesDataFormat = jsonFormat4(VehicleClassNamesData)
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
}

object PacketSize {
  val TELEMETRY_DATA: Short = 538
  val RACE_DATA: Short = 308
  val PARTICIPANTS_DATA: Short = 1040
  val TIMINGS_DATA: Short = 993
  val GAME_STATE_DATA: Short = 24
  val TIME_STATS_DATA: Short = 784
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
    packetVersion: Short // 11 what is the version of protocol for this handler, to be bumped with data structure change
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
    unfilteredThrottle: Short,
    unfilteredBrake: Short,
    unfilteredSteering: Byte,
    unfilteredClutch: Short
)

case class CarState(
    carFlags: Short,
    oilTempCelsius: Short,
    oilPressureKPa: Int,
    waterTempCelsius: Short,
    waterPressureKpa: Int,
    fuelPressureKpa: Int,
    fuelCapacity: Short,
    brake: Short,
    throttle: Short,
    clutch: Short,
    fuelLevel: Float,
    speed: Float,
    rpm: Int,
    maxRpm: Int,
    steering: Byte,
    gearNumGears: Short,
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

case class Tyre1(
    tyreFlags: Array[Short],
    terrain: Array[Short],
    tyreY: Array[Float],
    tyreRPS: Array[Float],
    tyreTemp: Array[Short],
    tyreHeightAboveGround: Array[Float],
    tyreWear: Array[Short],
    brakeDamage: Array[Short],
    suspensionDamage: Array[Short],
    brakeTempCelsius: Array[Short],
    tyreTreadTemp: Array[Int],
    tyreLayerTemp: Array[Int],
    tyreCarcassTemp: Array[Int],
    tyreRimTemp: Array[Int],
    tyreInternalAirTemp: Array[Int],
    tyreTempLeft: Array[Int],
    tyreTempCenter: Array[Int],
    tyreTempRight: Array[Int],
    wheelLocalPositionY: Array[Float],
    rideHeight: Array[Float],
    suspensionTravel: Array[Float],
    suspensionVelocity: Array[Float]
)
case class Tyre2(
    suspensionRideHeight: Array[Int],
    airPressure: Array[Int]
)

case class Tyre3(
    engineSpeed: Float,
    engineTorque: Float,
    wings: Array[Short],
    handBrake: Short
)

case class CarDamage(
    aeroDamage: Short,
    engineDamage: Short
)

case class HWState(
    joyPad0: Long,
    dPad: Short,
    tyreCompound: Array[String]
)

case class TelemetryData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.TELEMETRY_DATA,
    base: PacketBase,
    participantinfo: TelemetryParticipantInfo,
    unfilteredInput: UnfilteredInput,
    carState: CarState,
    velocity: Velocity,
    tyre1: Tyre1,
    tyre2: Tyre2,
    tyre3: Tyre3,
    carDamage: CarDamage,
    hwState: HWState
) {
  def toJsonString: String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Race stats data.  
//
//	Frequency: Logaritmic decrease
//	When it is sent: Counter resets on entering InRace state and again each time any of the values changes
//
*******************************************************************************************************************/
case class RaceData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.RACE_DATA,
    base: PacketBase,
    worldFastestLapTime: Float,
    personalFastestLapTime: Float,
    personalFastestSector1Time: Float,
    personalFastestSector2Time: Float,
    personalFastestSector3Time: Float,
    worldFastestSector1Time: Float,
    worldFastestSector2Time: Float,
    worldFastestSector3Time: Float,
    trackLength: Float,
    trackLocation: String,
    trackVariation: String,
    translatedTrackLocation: String,
    translatedTrackVariation: String,
    lapsTimeInEvent: Int, // contains lap number for lap based session or quantized session duration (number of 5mins) for timed sessions, the top bit is 1 for timed sessions
    enforcedPitStopLap: Byte
) {
  def toJsonString: String = this.toJson.toString
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
case class ParticipantsData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.PARTICIPANTS_DATA,
    base: PacketBase,
    participantsChangedTimestamp: Long,
    name: Array[String]
) {
  def toJsonString: String = this.toJson.toString
}

/*******************************************************************************************************************
//
//	Participant timings data.  
//
//	Frequency: Each tick of the UDP streamer how it is set in the options.
//	When it is sent: in race
//
*******************************************************************************************************************/
object RaceState {
  val RACESTATE_INVALID = 0
  val RACESTATE_NOT_STARTED = 1
  val RACESTATE_RACING = 2
  val RACESTATE_FINISHED = 3
  val RACESTATE_DISQUALIFIED = 4
  val RACESTATE_RETIRED = 5
  val RACESTATE_DNF = 6
}

case class ParticipantInfo(
    worldPosition: Array[Short],
    orientation: Array[Short], // Quantized heading (-PI .. +PI) , Quantized pitch (-PI / 2 .. +PI / 2),  Quantized bank (-PI .. +PI).
    currentLapDistance: Int,
    racePosition: Short, // holds the race position, + top bit shows if the participant is active or not
    sector: Short, // sector + extra precision bits for x/z position
    highestFlag: Short,
    pitModeSchedule: Short,
    carIndex: Int, // top bit shows if participant is (local or remote) human player or not
    raceState: Short, // race state flags + invalidated lap indication --
    currentLap: Short,
    currentTime: Float,
    currentSectorTime: Float
)

case class TimingsData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.TIMINGS_DATA,
    base: PacketBase,
    numParticipants: Byte,
    participantsChangedTimestamp: Long,
    eventTimeRemaining: Float, // time remaining, -1 for invalid time,  -1 - laps remaining in lap based races  --
    splitTimeAhead: Float,
    splitTimeBehind: Float,
    splitTime: Float,
    partcipants: Array[ParticipantInfo]
) {
  def toJsonString: String = this.toJson.toString
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
object GameState {
  val GAME_EXITED = 0
  val GAME_FRONT_END = 1
  val GAME_INGAME_PLAYING = 2
  val GAME_INGAME_PAUSED = 3
  val GAME_INGAME_INMENU_TIME_TICKING = 4
  val GAME_INGAME_RESTARTING = 5
  val GAME_INGAME_REPLAY = 6
  val GAME_FRONT_END_REPLAY = 7
}

object SessionState {
  val SESSION_INVALID = 0
  val SESSION_PRACTICE = 1
  val SESSION_TEST = 2
  val SESSION_QUALIFY = 3
  val SESSION_FORMATION_LAP = 4
  val SESSION_RACE = 5
  val SESSION_TIME_ATTACK = 6
}

case class GameStateData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.GAME_STATE_DATA,
    base: PacketBase,
    buildVersionNumber: Int,
    gameState: Byte, // first 3 bits are used for game state enum, second 3 bits for session state enum See shared memory example file for the enums
    sessionState: Byte,
    ambientTemperature: Byte,
    trackTemperature: Byte,
    rainDensity: Short,
    snowDensity: Short,
    windSpeed: Byte,
    windDirectionX: Byte,
    windDirectionY: Byte // 22 padded to 24
) {
  def toJsonString: String = this.toJson.toString
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
case class ParticipantStatsInfo(
    fastestLapTime: Float,
    lastLapTime: Float,
    lastSectorTime: Float,
    fastestSector1Time: Float,
    fastestSector2Time: Float,
    fastestSector3Time: Float
)

case class ParticipantsStats(
    participants: Array[ParticipantStatsInfo]
)

case class TimeStatsData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.TIME_STATS_DATA,
    base: PacketBase,
    participantsChangedTimestamp: Long,
    stats: ParticipantsStats
) {
  def toJsonString: String = this.toJson.toString
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

case class ParticipantVehicleNamesData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.PARTICIPANT_VEHICLE_NAMES_DATA,
    base: PacketBase,
    vehicles: Array[VehicleInfo]
) {
  def toJsonString: String = this.toJson.toString
}

case class ClassInfo(
    classIndex: Long,
    name: String
)

case class VehicleClassNamesData(
    time: Long = System.currentTimeMillis,
    size: Short = PacketSize.VEHICLE_CLASS_NAMES_DATA,
    base: PacketBase,
    classes: Array[ClassInfo]
) {
  def toJsonString: String = this.toJson.toString
}
