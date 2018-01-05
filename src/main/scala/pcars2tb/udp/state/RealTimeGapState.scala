package pcars2tb.udp.state

import com.typesafe.scalalogging.LazyLogging
import pcars2tb.util.BigDecimalSupport._
import pcars2tb.udp.listener.{
  RaceStateDefine,
  PitModeDefine,
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
    packetType = UdpStreamerPacketHandlerType.REAL_TIME_GAP,
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
      sessionState = gameStateData.sessionState.value,
      viewedParticipantIndex = 0,
      trackLength = 0f,
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
    sessionState: Byte,
    viewedParticipantIndex: Byte,
    trackLength: Float,
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
        (nextState, Some(nextState.toUdpData()))
      case udpData: RaceData =>
        val nextState = mergeRaceData(udpData)
        (nextState, Some(nextState.toUdpData()))
      case udpData: TelemetryData if (isPlaying) =>
        val nextState = mergeTelemetryData(udpData)
        (nextState, None)
      case udpData: TimingsData if (isPlaying) =>
        val nextState = mergeTimingsData(udpData)
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimingsData =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isPlaying) =>
        val nextState = mergeTimeStatsData(udpData)
        (nextState, None)
      case udpData: TimeStatsData =>
        val nextState = resetState()
        (nextState, None)
      case _ => (this, None)
    }

  def toUdpData(): RealTimeGap = {
    (currentList.lastOption, fastestRemainingList.headOption) match {
      case (Some(current), Some(fastest)) =>
        val gapTime = current.time - fastest.time
        RealTimeGap(
          base = base,
          gapTime = gapTime.toMinuteFormatFromSecondsWithSigned
        )
      case _ =>
        RealTimeGap(
          base = base,
          gapTime = "--:--.---"
        )
    }
  }

  private def resetGameStateData(gameStateData: GameStateData) =
    if (sessionState == gameStateData.sessionState.value)
      RealTimeGapState(
        isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
        isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
          || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
        isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
        sessionState = gameStateData.sessionState.value,
        viewedParticipantIndex = viewedParticipantIndex,
        trackLength = trackLength,
        currentLap = currentLap,
        currentList = currentList,
        lastList = lastList,
        fastestTime = fastestTime,
        fastestList = fastestList,
        fastestRemainingList = fastestRemainingList
      )
    else
      RealTimeGapState(
        isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
        isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
          || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
        isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
        sessionState = gameStateData.sessionState.value,
        viewedParticipantIndex = 0,
        trackLength = trackLength,
        currentLap = 0,
        currentList = emptyTimeAndDistanceList,
        lastList = None,
        fastestTime = None,
        fastestList = emptyTimeAndDistanceList,
        fastestRemainingList = emptyTimeAndDistanceList
      )

  private def resetState() =
    RealTimeGapState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      sessionState = sessionState,
      viewedParticipantIndex = 0,
      trackLength = trackLength,
      currentLap = 0,
      currentList = emptyTimeAndDistanceList,
      lastList = None,
      fastestTime = None,
      fastestList = emptyTimeAndDistanceList,
      fastestRemainingList = emptyTimeAndDistanceList
    )

  private def mergeRaceData(raceData: RaceData) =
    RealTimeGapState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      sessionState = sessionState,
      viewedParticipantIndex = viewedParticipantIndex,
      trackLength = raceData.trackLength,
      currentLap = currentLap,
      currentList = currentList,
      lastList = lastList,
      fastestTime = fastestTime,
      fastestList = fastestList,
      fastestRemainingList = fastestRemainingList
    )

  private def mergeTelemetryData(telemetryData: TelemetryData) =
    telemetryData.participantInfo.viewedParticipantIndex match {
      case index if (index != viewedParticipantIndex) =>
        RealTimeGapState(
          isMenu = isMenu,
          isPlaying = isPlaying,
          isRestart = isRestart,
          sessionState = sessionState,
          viewedParticipantIndex = index,
          trackLength = trackLength,
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
    if (participantInfo.raceState != RaceStateDefine.RACESTATE_RACING)
      RealTimeGapState(
        isMenu = isMenu,
        isPlaying = isPlaying,
        isRestart = isRestart,
        sessionState = sessionState,
        viewedParticipantIndex = viewedParticipantIndex,
        trackLength = trackLength,
        currentLap = participantInfo.currentLap,
        currentList = emptyTimeAndDistanceList,
        lastList = None,
        fastestTime = None,
        fastestList = emptyTimeAndDistanceList,
        fastestRemainingList = emptyTimeAndDistanceList
      )
    else if (participantInfo.currentTime > 0f
      && (participantInfo.pitMode == PitModeDefine.PIT_MODE_NONE
        || participantInfo.pitMode == PitModeDefine.PIT_MODE_DRIVING_INTO_PITS
        || participantInfo.pitMode == PitModeDefine.PIT_MODE_IN_PIT
        || participantInfo.pitMode == PitModeDefine.PIT_MODE_DRIVING_OUT_OF_PITS))
      participantInfo.currentLap match {
        case lap if (lap < 1) => this
        case lap if (lap == 1) =>
          RealTimeGapState(
            isMenu = isMenu,
            isPlaying = isPlaying,
            isRestart = isRestart,
            sessionState = sessionState,
            viewedParticipantIndex = viewedParticipantIndex,
            trackLength = trackLength,
            currentLap = participantInfo.currentLap,
            currentList = currentList :+ TimeAndDistance(
              time = participantInfo.currentTime,
              distance = participantInfo.currentLapDistance
            ),
            lastList = lastList,
            fastestTime = fastestTime,
            fastestList = fastestList,
            fastestRemainingList = fastestList
          )
        case lap if (lap == currentLap) =>
          if (participantInfo.currentLapDistance > trackLength) this
          else RealTimeGapState(
            isMenu = isMenu,
            isPlaying = isPlaying,
            isRestart = isRestart,
            sessionState = sessionState,
            viewedParticipantIndex = viewedParticipantIndex,
            trackLength = trackLength,
            currentLap = currentLap,
            currentList = currentList :+ TimeAndDistance(
              time = participantInfo.currentTime,
              distance = participantInfo.currentLapDistance
            ),
            lastList = lastList,
            fastestTime = fastestTime,
            fastestList = fastestList,
            fastestRemainingList = fastestRemainingList.dropWhile(_.distance < participantInfo.currentLapDistance)
          )
        case _ =>
          RealTimeGapState(
            isMenu = isMenu,
            isPlaying = isPlaying,
            isRestart = isRestart,
            sessionState = sessionState,
            viewedParticipantIndex = viewedParticipantIndex,
            trackLength = trackLength,
            currentLap = participantInfo.currentLap,
            currentList = List(TimeAndDistance(
              time = participantInfo.currentTime,
              distance = participantInfo.currentLapDistance
            )),
            lastList = Some(currentList),
            fastestTime = fastestTime,
            fastestList = fastestList,
            fastestRemainingList = fastestList
          )
      }
    else RealTimeGapState(
      isMenu = isMenu,
      isPlaying = isPlaying,
      isRestart = isRestart,
      sessionState = sessionState,
      viewedParticipantIndex = viewedParticipantIndex,
      trackLength = trackLength,
      currentLap = participantInfo.currentLap,
      currentList = emptyTimeAndDistanceList,
      lastList = None,
      fastestTime = fastestTime,
      fastestList = fastestList,
      fastestRemainingList = fastestList
    )
  }

  private def mergeTimeStatsData(timeStatsData: TimeStatsData) = {
    val stat = timeStatsData.stats.participants(viewedParticipantIndex)
    if (stat.lastLapTime < 0f) this
    else lastList match {
      case Some(last) =>
        fastestTime match {
          case Some(fastest) if (fastest > stat.lastLapTime) =>
            RealTimeGapState(
              isMenu = isMenu,
              isPlaying = isPlaying,
              isRestart = isRestart,
              sessionState = sessionState,
              viewedParticipantIndex = viewedParticipantIndex,
              trackLength = trackLength,
              currentLap = currentLap,
              currentList = currentList,
              lastList = None,
              fastestTime = Some(stat.lastLapTime),
              fastestList = last,
              fastestRemainingList = last
            )
          case Some(_) =>
            RealTimeGapState(
              isMenu = isMenu,
              isPlaying = isPlaying,
              isRestart = isRestart,
              sessionState = sessionState,
              viewedParticipantIndex = viewedParticipantIndex,
              trackLength = trackLength,
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
              sessionState = sessionState,
              viewedParticipantIndex = viewedParticipantIndex,
              trackLength = trackLength,
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
