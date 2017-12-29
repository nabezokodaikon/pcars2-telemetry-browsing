package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.util.BigDecimalSupport._
import pcars2tb.udp.listener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  UdpData,
  GameStateData,
  TelemetryData,
  RaceData,
  TimingsData,
  RealTimeGap
}

final case class TimeAndDistance(
    time: List[Float],
    distance: List[Int]
)

final object RealTimeGapState {

  val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.LAP_TIME_DETAILS,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )
}

final case class RealTimeGapState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    current: TimeAndDistance,
    fastest: TimeAndDistance,
    fastestRemaining: TimeAndDistance
) extends LazyLogging {
  import RealTimeGapState._
}
