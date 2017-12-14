package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.listener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  UdpData,
  GameStateData,
  TelemetryData,
  RaceData,
  ParticipantInfo,
  TimingsData,
  ParticipantStatsInfo,
  TimeStatsData,
  AggregateTime
}
import pcars2tb.util.BigDecimalSupport._

final object TimeAggregateState {
  val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.AGGREGATE_TIME,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )

  def createInitialState(gameStateData: GameStateData): TimeAggregateState =
    TimeAggregateState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      totalTimes = TotalTime.emptyArray
    )
}

final case class TimeAggregateState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    totalTimes: Array[TotalTime]
) {
  import TimeAggregateState._

  def createNextState(udpData: UdpData): (TimeAggregateState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetState(udpData)
        (nextState, None)
      case udpData: RaceData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TelemetryData if (isPlaying) =>
        if (udpData.participantInfo.viewedParticipantIndex != viewedParticipantIndex) {
          val nextState = mergeViewedParticipantIndex(udpData.participantInfo.viewedParticipantIndex)
          (nextState, None)
        } else {
          (this, None)
        }
      case udpData: TimingsData if (isPlaying) =>
        mergeState(udpData) match {
          case Some(nextState) => (nextState, Some(nextState.toUdpData()))
          case None => (this, None)
        }
      case udpData: TimeStatsData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isRestart) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isPlaying) =>
        mergeState(udpData) match {
          case Some(nextState) => (nextState, Some(nextState.toUdpData()))
          case None => (this, None)
        }
      case _ => (this, None)
    }

  def toUdpData(): AggregateTime = {
    val cumulativeTimes = for {
      i <- totalTimes
      if (i.currentSectorTime > 0f)
    } yield i.currentSectorTime + i.cumulativeTime

    val fastestTime = if (cumulativeTimes.length > 0) cumulativeTimes.min else 0f
    val viewedParticipantTotalTime = totalTimes(viewedParticipantIndex)
    val totalTime = viewedParticipantTotalTime.currentSectorTime + viewedParticipantTotalTime.cumulativeTime
    val gapTime = totalTime - fastestTime

    AggregateTime(
      base = base,
      totalTime = totalTime.toMinuteFormatFromSeconds,
      gapTime = gapTime.toMinuteFormatFromSeconds
    )
  }

  def resetState(gameStateData: GameStateData): TimeAggregateState =
    TimeAggregateState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = this.viewedParticipantIndex,
      totalTimes = this.totalTimes
    )

  def resetState(): TimeAggregateState =
    TimeAggregateState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      viewedParticipantIndex = this.viewedParticipantIndex,
      totalTimes = TotalTime.emptyArray
    )

  def mergeViewedParticipantIndex(viewedParticipantIndex: Byte): TimeAggregateState =
    TimeAggregateState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      viewedParticipantIndex = viewedParticipantIndex,
      totalTimes = TotalTime.emptyArray
    )

  def mergeState(timingsData: TimingsData): Option[TimeAggregateState] = {
    if (this.totalTimes.length < 32 || timingsData.participants.length < 32) {
      return None
    }

    val participant = timingsData.participants(this.viewedParticipantIndex)
    if (participant.currentTime < 0) {
      return None
    }

    val totalTimes =
      for (
        i <- 0 to 31
      ) yield mergeTotalTime(this.totalTimes(i), timingsData.participants(i))

    Some(TimeAggregateState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      viewedParticipantIndex = this.viewedParticipantIndex,
      totalTimes = totalTimes.toArray
    ))
  }

  def mergeTotalTime(totalTime: TotalTime, participantInfo: ParticipantInfo): TotalTime =
    TotalTime(
      sector = participantInfo.sector,
      lastSector =
        if (totalTime.sector != participantInfo.sector) totalTime.lastSector
        else totalTime.sector,
      currentSectorTime = participantInfo.currentSectorTime,
      cumulativeTime = totalTime.cumulativeTime
    )

  def mergeState(timeStatsData: TimeStatsData): Option[TimeAggregateState] = {
    if (this.totalTimes.length < 32 || timeStatsData.stats.participants.length < 32) {
      return None
    }

    val totalTimes =
      for (
        i <- 0 to 31
      ) yield mergeTotalTime(this.totalTimes(i), timeStatsData.stats.participants(i))

    Some(TimeAggregateState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      viewedParticipantIndex = this.viewedParticipantIndex,
      totalTimes = totalTimes.toArray
    ))
  }

  def mergeTotalTime(totalTime: TotalTime, participantInfo: ParticipantStatsInfo): TotalTime = {
    if (totalTime.sector == totalTime.lastSector) {
      return totalTime
    }

    val lastSectorTime = if (participantInfo.lastSectorTime > 0f) participantInfo.lastSectorTime else 0f

    TotalTime(
      sector = totalTime.sector,
      lastSector = totalTime.lastSector,
      currentSectorTime = totalTime.currentSectorTime,
      cumulativeTime = totalTime.cumulativeTime + lastSectorTime
    )
  }
}

final object TotalTime {
  val empty: TotalTime = TotalTime(0, 0, 0f, 0f)
  val emptyArray: Array[TotalTime] = Array.fill(32)(empty)
}

final case class TotalTime(
    sector: Short,
    lastSector: Short,
    currentSectorTime: Float,
    cumulativeTime: Float
)
