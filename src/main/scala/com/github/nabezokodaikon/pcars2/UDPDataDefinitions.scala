package com.github.nabezokodaikon.pcars2

object EUDPStreamerPacketHandlerType {
  val CAR_PHYSICS: Byte = 0 // TelemetryData
  val RACE_DEFINITION: Byte = 1
  val PARTICIPANTS: Byte = 2
  val TIMINGS: Byte = 3 // TimingsData
  val GAME_STATE: Byte = 4 // GameState
  val WEATHER_STATE: Byte = 5 // not sent at the moment, information can be found in the game state packet
  val VEHICLE_NAMES: Byte = 6 // not sent at the moment
  val TIME_STATS: Byte = 7
  val PARTICIPANT_VEHICLE_NAMES: Byte = 8
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
