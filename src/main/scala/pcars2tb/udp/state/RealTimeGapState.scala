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
  TimeStatsData,
  RealTimeGap
}

final case class TimeAndDistance(
    time: Float,
    distance: Int
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

  val emptyTimeAndDistanceList = List[TimeAndDistance]()

  def createInitialState(gameStateData: GameStateData): RealTimeGapState =
    RealTimeGapState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      currentLap = 0,
      currentList = emptyTimeAndDistanceList,
      lastList = None,
      fastestTime = None,
      fastestList = emptyTimeAndDistanceList,
      fastestRemainingList = emptyTimeAndDistanceList
    )
}

final case class RealTimeGapState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    currentLap: Short,
    currentList: List[TimeAndDistance],
    lastList: Option[List[TimeAndDistance]],
    fastestTime: Option[Float],
    fastestList: List[TimeAndDistance],
    fastestRemainingList: List[TimeAndDistance]
) extends LazyLogging {
  import RealTimeGapState._

  def createNextState(udpData: UdpData): (RealTimeGapState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetGameStateData(udpData)
        (nextState, None)
      case udpData: RaceData if (isMenu) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TelemetryData if (isPlaying) =>
        val nextState = mergeTelemetryData(udpData)
        (nextState, None)
      case udpData: TimingsData if (isMenu) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimingsData if (isRestart) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimingsData if (isPlaying) =>
        val nextState = mergeTimingsData(udpData)
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isMenu) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimeStatsData if (isRestart) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimeStatsData if (isPlaying) =>
        val nextState = mergeTimeStatsData(udpData)
        (nextState, None)
      case _ => (this, None)
    }

  def toUdpData(): RealTimeGap =
    (currentList.lastOption, fastestRemainingList.headOption) match {
      case (Some(current), Some(fastest)) =>
        val gapTime = current.time - fastest.time
        val isMinus = (gapTime < 0)
        RealTimeGap(
          base = base,
          gapTime = gapTime.toMinuteFormatFromSecondsWithSigned,
          isMinus = isMinus
        )
      case _ =>
        RealTimeGap(
          base = base,
          gapTime = "--:--.---",
          isMinus = true
        )
    }

  private def resetGameStateData(gameStateData: GameStateData) =
    RealTimeGapState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = viewedParticipantIndex,
      currentLap = currentLap,
      currentList = currentList,
      lastList = lastList,
      fastestTime = fastestTime,
      fastestList = fastestList,
      fastestRemainingList = fastestRemainingList
    )

  private def resetState() =
    RealTimeGapState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      viewedParticipantIndex = 0,
      currentLap = 0,
      currentList = emptyTimeAndDistanceList,
      lastList = None,
      fastestTime = None,
      fastestList = emptyTimeAndDistanceList,
      fastestRemainingList = emptyTimeAndDistanceList
    )

  private def mergeTelemetryData(telemetryData: TelemetryData) =
    telemetryData.participantInfo.viewedParticipantIndex match {
      case index if (index != viewedParticipantIndex) =>
        RealTimeGapState(
          isMenu = isMenu,
          isPlaying = isPlaying,
          isRestart = isRestart,
          viewedParticipantIndex = index,
          currentLap = 0,
          currentList = emptyTimeAndDistanceList,
          lastList = None,
          fastestTime = None,
          fastestList = emptyTimeAndDistanceList,
          fastestRemainingList = emptyTimeAndDistanceList
        )
      case _ => this
    }

  private def mergeTimingsData(timingsData: TimingsData) = {
    val participantInfo = timingsData.participants(viewedParticipantIndex)
    if (currentLap == participantInfo.currentLap) {
      RealTimeGapState(
        isMenu = isMenu,
        isPlaying = isPlaying,
        isRestart = isRestart,
        viewedParticipantIndex = viewedParticipantIndex,
        currentLap = currentLap,
        currentList = currentList :+ TimeAndDistance(
          time = participantInfo.currentTime,
          distance = participantInfo.currentLapDistance
        ),
        lastList = lastList,
        fastestTime = fastestTime,
        fastestList = fastestList,
        fastestRemainingList = fastestRemainingList.dropWhile(a => a.distance <= participantInfo.currentLapDistance)
      )
    } else {
      RealTimeGapState(
        isMenu = isMenu,
        isPlaying = isPlaying,
        isRestart = isRestart,
        viewedParticipantIndex = viewedParticipantIndex,
        currentLap = participantInfo.currentLap,
        currentList = emptyTimeAndDistanceList :+ TimeAndDistance(
          time = participantInfo.currentTime,
          distance = participantInfo.currentLapDistance
        ),
        lastList = Some(currentList),
        fastestTime = fastestTime,
        fastestList = fastestList,
        fastestRemainingList = fastestList
      )
    }
  }

  private def mergeTimeStatsData(timeStatsData: TimeStatsData) = {
    lastList match {
      case Some(last) =>
        val stat = timeStatsData.stats.participants(viewedParticipantIndex)
        fastestTime match {
          case Some(fastest) if (stat.lastLapTime >= fastest) =>
            RealTimeGapState(
              isMenu = isMenu,
              isPlaying = isPlaying,
              isRestart = isRestart,
              viewedParticipantIndex = viewedParticipantIndex,
              currentLap = currentLap,
              currentList = currentList,
              lastList = None,
              fastestTime = fastestTime,
              fastestList = fastestList,
              fastestRemainingList = fastestList
            )
          case _ =>
            RealTimeGapState(
              isMenu = isMenu,
              isPlaying = isPlaying,
              isRestart = isRestart,
              viewedParticipantIndex = viewedParticipantIndex,
              currentLap = currentLap,
              currentList = currentList,
              lastList = None,
              fastestTime = Some(stat.lastLapTime),
              fastestList = last,
              fastestRemainingList = last
            )
        }
      case None => this
    }
  }
}
