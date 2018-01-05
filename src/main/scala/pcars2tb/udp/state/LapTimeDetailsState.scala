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
  LapTime,
  LapTimeDetails
}

final object LapTimeDetailsState {
  val emptyLap = "---"
  val emptyTime = "--:--.---"
  val emptyLapTime = LapTime(
    lap = emptyLap,
    sector1 = emptyTime,
    sector2 = emptyTime,
    sector3 = emptyTime,
    lapTime = emptyTime,
    delta = emptyTime
  )

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

  def createInitialState(gameStateData: GameStateData): LapTimeDetailsState =
    LapTimeDetailsState(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      sessionState = gameStateData.sessionState.value,
      isTimedSessions = false,
      lapsInEvent = 0,
      viewedParticipantIndex = 0,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )
}

final case class LapTimeDetailsState(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    sessionState: Byte,
    isTimedSessions: Boolean,
    lapsInEvent: Int,
    viewedParticipantIndex: Byte,
    currentView: Option[LapData],
    currentData: Option[CurrentData],
    currentLapData: Option[LapData],
    lapDataList: List[LapData]
) extends LazyLogging {
  import LapTimeDetailsState._

  def createNextState(udpData: UdpData): (LapTimeDetailsState, Option[UdpData]) =
    udpData match {
      case udpData: GameStateData =>
        val nextState = resetStateByGameStateData(udpData)
        (nextState, Some(toUdpData()))
      case udpData: RaceData if (isMenu) =>
        val nextState = resetStateByRaceData(udpData)
        (nextState, Some(nextState.toUdpData()))
      case udpData: TelemetryData if (isPlaying) =>
        if (udpData.participantInfo.viewedParticipantIndex != viewedParticipantIndex) {
          val nextState = resetStateByViewedParticipantIndex(udpData.participantInfo.viewedParticipantIndex)
          (nextState, None)
        } else {
          (this, None)
        }
      case udpData: TimingsData if (isMenu && currentData != None) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimingsData if (isRestart && currentData != None) =>
        val nextState = resetState()
        (nextState, None)
      case udpData: TimingsData if (isPlaying) =>
        createState(udpData) match {
          case Some(nextState) => (nextState, None)
          case None => (this, None)
        }
      case udpData: TimeStatsData if (isMenu) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isRestart) =>
        val nextState = resetState()
        (nextState, Some(nextState.toUdpData()))
      case udpData: TimeStatsData if (isPlaying) =>
        createState(udpData) match {
          case Some(nextState) => (nextState, Some(nextState.toUdpData()))
          case None => (this, None)
        }
      case _ => (this, None)
    }

  def toUdpData(): LapTimeDetails = {
    val current = currentLapData match {
      case Some(a) => a.toLapTime
      case None => emptyLapTime
    }

    lapDataList.length match {
      case 0 =>
        LapTimeDetails(
          base = base,
          isTimedSessions = isTimedSessions,
          lapsInEvent = lapsInEvent,
          current = current,
          fastest = emptyLapTime,
          average = emptyLapTime,
          history = List[LapTime]()
        )
      case _ =>
        val fastest = lapDataList.reduceLeft((a, b) => {
          val lapTimeA = a.lapTime match {
            case Some(lapTime) => lapTime
            case None => Int.MaxValue
          }
          val lapTimeB = b.lapTime match {
            case Some(lapTime) => lapTime
            case None => Int.MaxValue
          }
          if (lapTimeA < lapTimeB) a else b
        }).toLapTime

        val historyLength = lapDataList.length.toFloat
        val averageSector1 = lapDataList.flatMap(_.sector1).sum / historyLength
        val averageSector2 = lapDataList.flatMap(_.sector2).sum / historyLength
        val averageSector3 = lapDataList.flatMap(_.sector3).sum / historyLength
        val averageLapTime = lapDataList.flatMap(_.lapTime).sum / historyLength
        val average = LapTime(
          lap = LapTimeDetailsState.emptyLap,
          sector1 = averageSector1.toMinuteFormatFromSeconds,
          sector2 = averageSector2.toMinuteFormatFromSeconds,
          sector3 = averageSector3.toMinuteFormatFromSeconds,
          lapTime = averageLapTime.toMinuteFormatFromSeconds,
          delta = emptyTime
        )

        val history = for (i <- 1 to 60) yield {
          (lapDataList.takeRight(i), lapDataList.takeRight(i + 1)) match {
            case ((lapData1 :: Nil), (_ :: Nil)) => Some(lapData1.toLapTime)
            case ((lapData1 :: _), (lapData2 :: _)) if (lapData1.lap > lapData2.lap) =>
              (lapData1.lapTime, lapData2.lapTime) match {
                case (Some(lapTime1), Some(lapTime2)) =>
                  val delta = lapTime1 - lapTime2
                  Some(lapData1.toLapTime(delta))
                case _ => Some(lapData1.toLapTime)
              }
            case ((lapData1 :: _), _) => Some(lapData1.toLapTime)
            case _ => None
          }
        }

        LapTimeDetails(
          base = base,
          isTimedSessions = isTimedSessions,
          lapsInEvent = lapsInEvent,
          current = current,
          fastest = fastest,
          average = average,
          history = history
            .toList
            .flatMap(i => i)
            .reverse
            .takeRight(60.min(lapDataList.length))
        )
    }
  }

  def resetStateByGameStateData(gameStateData: GameStateData): LapTimeDetailsState =
    if (sessionState == gameStateData.sessionState.value)
      LapTimeDetailsState(
        isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
        isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
          || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
        isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
        sessionState = this.sessionState,
        isTimedSessions = this.isTimedSessions,
        lapsInEvent = this.lapsInEvent,
        viewedParticipantIndex = this.viewedParticipantIndex,
        currentView = this.currentView,
        currentData = this.currentData,
        currentLapData = this.currentLapData,
        lapDataList = this.lapDataList
      )
    else
      LapTimeDetailsState(
        isMenu = this.isMenu,
        isPlaying = this.isPlaying,
        isRestart = this.isRestart,
        sessionState = gameStateData.sessionState.value,
        isTimedSessions = false,
        lapsInEvent = 0,
        viewedParticipantIndex = 0,
        currentView = None,
        currentData = None,
        currentLapData = None,
        lapDataList = List[LapData]()
      )

  def resetStateByRaceData(raceData: RaceData): LapTimeDetailsState =
    LapTimeDetailsState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      sessionState = this.sessionState,
      isTimedSessions = raceData.isTimedSessions,
      lapsInEvent = raceData.lapsInEvent,
      viewedParticipantIndex = this.viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  def resetStateByViewedParticipantIndex(viewedParticipantIndex: Byte): LapTimeDetailsState =
    LapTimeDetailsState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      sessionState = this.sessionState,
      isTimedSessions = this.isTimedSessions,
      lapsInEvent = this.lapsInEvent,
      viewedParticipantIndex = viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  def resetState(): LapTimeDetailsState =
    LapTimeDetailsState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      sessionState = this.sessionState,
      isTimedSessions = this.isTimedSessions,
      lapsInEvent = this.lapsInEvent,
      viewedParticipantIndex = this.viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  def createState(timingsData: TimingsData): Option[LapTimeDetailsState] = {
    val participant = timingsData.participants(this.viewedParticipantIndex)
    if (participant.currentTime < 0) {
      return None
    }

    this.currentData match {
      case Some(currentData) =>
        if (participant.currentLap != currentData.currentLap
          || participant.sector != currentData.sector) {
          val newCurrentData = CurrentData(
            participant.currentLap, participant.sector,
            participant.currentTime, participant.currentSectorTime
          )
          Some(mergeState(newCurrentData))
        } else {
          None
        }
      case None =>
        val newCurrentData = CurrentData(
          participant.currentLap, participant.sector,
          participant.currentTime, participant.currentSectorTime
        )
        Some(mergeState(newCurrentData))
    }
  }

  def createState(timeStatsData: TimeStatsData): Option[LapTimeDetailsState] =
    (this.currentData, this.currentLapData) match {
      case (Some(currentData), Some(currentLapData)) =>
        currentData.currentLap match {
          case currentLap if (currentLap == currentLapData.lap) =>
            currentData.sector match {
              case 1 =>
                val lapData = LapData(currentData.currentLap, None, None, None, None)
                Some(mergeState(lapData))
              case 2 =>
                val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
                Some(mergeState(lapData))
              case 3 =>
                val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, currentLapData.sector1, Some(stat.lastSectorTime), None, None)
                Some(mergeState(lapData))
              case _ =>
                logger.warn(s"Received unknown sector: ${currentData.sector}")
                Some(resetState())
            }
          case currentLap if (currentLap > currentLapData.lap) =>
            (currentLapData.sector1, currentLapData.sector2, currentLapData.sector3) match {
              case (Some(_), Some(_), Some(_)) =>
                Some(this)
              case (Some(_), Some(_), None) =>
                val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, currentLapData.sector2, Some(stat.lastSectorTime), Some(stat.lastLapTime)
                )
                Some(addCurrentLapData(lapData))
              case (Some(_), None, None) =>
                val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, Some(stat.lastSectorTime), None, Some(stat.lastLapTime)
                )
                Some(addCurrentLapData(lapData))
              case (None, None, None) =>
                val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, Some(stat.lastSectorTime), None, None, Some(stat.lastLapTime)
                )
                Some(addCurrentLapData(lapData))
              case _ =>
                logger.warn("Current data is inconsistent.")
                Some(resetState())
            }
          case _ =>
            logger.warn("Current lap is inconsistent.")
            Some(resetState())
        }
      case (Some(currentData), None) =>
        currentData.sector match {
          case 1 =>
            val lapData = LapData(currentData.currentLap, None, None, None, None)
            Some(mergeState(lapData))
          case 2 =>
            val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
            Some(mergeState(lapData))
          case 3 =>
            val stat = timeStatsData.stats.participants(this.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, None, Some(stat.lastSectorTime), None, None)
            Some(mergeState(lapData))
          case _ =>
            logger.warn(s"Received unknown sector: ${currentData.sector}")
            Some(resetState())
        }
      case _ => None
    }

  def mergeState(currentData: CurrentData): LapTimeDetailsState = {
    val currentView = this.currentLapData match {
      case Some(currentLapData) => currentData.sector match {
        case 1 =>
          Some(LapData(
            currentData.currentLap,
            Some(currentData.currentSectorTime), None, None,
            Some(currentData.currentTime)
          ))
        case 2 =>
          Some(LapData(
            currentData.currentLap,
            currentLapData.sector1, Some(currentData.currentSectorTime), None,
            Some(currentData.currentTime)
          ))
        case 3 =>
          Some(LapData(
            currentData.currentLap,
            currentLapData.sector1, currentLapData.sector2, Some(currentData.currentSectorTime),
            Some(currentData.currentTime)
          ))
        case _ =>
          logger.warn(s"Received unknown sector: ${currentData.sector}")
          None
      }
      case None => currentData.sector match {
        case 1 =>
          Some(LapData(
            currentData.currentLap,
            Some(currentData.currentSectorTime), None, None,
            Some(currentData.currentTime)
          ))
        case 2 =>
          Some(LapData(
            currentData.currentLap,
            None, Some(currentData.currentSectorTime), None,
            Some(currentData.currentTime)
          ))
        case 3 =>
          Some(LapData(
            currentData.currentLap,
            None, None, Some(currentData.currentSectorTime),
            Some(currentData.currentTime)
          ))
        case _ =>
          logger.warn(s"Received unknown sector: ${currentData.sector}")
          None
      }
    }

    LapTimeDetailsState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      sessionState = this.sessionState,
      isTimedSessions = this.isTimedSessions,
      lapsInEvent = this.lapsInEvent,
      viewedParticipantIndex = this.viewedParticipantIndex,
      currentView = currentView,
      currentData = Some(currentData),
      currentLapData = this.currentLapData,
      lapDataList = this.lapDataList
    )
  }

  def mergeState(currentLapData: LapData): LapTimeDetailsState =
    LapTimeDetailsState(
      isMenu = this.isMenu,
      isPlaying = this.isPlaying,
      isRestart = this.isRestart,
      sessionState = this.sessionState,
      isTimedSessions = this.isTimedSessions,
      lapsInEvent = this.lapsInEvent,
      viewedParticipantIndex = this.viewedParticipantIndex,
      currentView = this.currentView,
      currentData = this.currentData,
      currentLapData = Some(currentLapData),
      lapDataList = this.lapDataList
    )

  def addCurrentLapData(currentLapData: LapData): LapTimeDetailsState =
    (this.isTimedSessions, this.lapsInEvent) match {
      case (isTimedSessions, lapsInEvent) if (isTimedSessions) =>
        // Time session.
        LapTimeDetailsState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          sessionState = this.sessionState,
          isTimedSessions = this.isTimedSessions,
          lapsInEvent = this.lapsInEvent,
          viewedParticipantIndex = this.viewedParticipantIndex,
          currentView = this.currentView,
          currentData = this.currentData,
          currentLapData = None,
          lapDataList = this.lapDataList :+ currentLapData
        )
      case (isTimedSessions, lapsInEvent) if (!isTimedSessions && lapsInEvent == 0) =>
        // Practice.
        LapTimeDetailsState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          sessionState = this.sessionState,
          isTimedSessions = this.isTimedSessions,
          lapsInEvent = this.lapsInEvent,
          viewedParticipantIndex = this.viewedParticipantIndex,
          currentView = this.currentView,
          currentData = this.currentData,
          currentLapData = None,
          lapDataList = this.lapDataList :+ currentLapData
        )
      case (isTimedSessions, lapsInEvent) if (!isTimedSessions && lapsInEvent >= this.lapDataList.length) =>
        // Laps session.
        LapTimeDetailsState(
          isMenu = this.isMenu,
          isPlaying = this.isPlaying,
          isRestart = this.isRestart,
          sessionState = this.sessionState,
          isTimedSessions = this.isTimedSessions,
          lapsInEvent = this.lapsInEvent,
          viewedParticipantIndex = this.viewedParticipantIndex,
          currentView = this.currentView,
          currentData = this.currentData,
          currentLapData = None,
          lapDataList = this.lapDataList :+ currentLapData
        )
      case _ =>
        this
    }
}

final case class CurrentData(
    currentLap: Short,
    sector: Short,
    currentTime: Float,
    currentSectorTime: Float
)

final case class LapData(
    lap: Short,
    sector1: Option[Float],
    sector2: Option[Float],
    sector3: Option[Float],
    lapTime: Option[Float]
) {
  def toLapTime(): LapTime = {
    LapTime(
      lap = s"${lap}",
      sector1 = toTimeString(sector1),
      sector2 = toTimeString(sector2),
      sector3 = toTimeString(sector3),
      lapTime = toTimeString(lapTime),
      delta = LapTimeDetailsState.emptyTime
    )
  }

  def toLapTime(delta: Float): LapTime = {
    LapTime(
      lap = s"${lap}",
      sector1 = toTimeString(sector1),
      sector2 = toTimeString(sector2),
      sector3 = toTimeString(sector3),
      lapTime = toTimeString(lapTime),
      delta = delta.toMinuteFormatFromSecondsWithSigned
    )
  }

  private def toTimeString(value: Option[Float]): String = {
    value match {
      case Some(v) => v.toMinuteFormatFromSeconds
      case None => LapTimeDetailsState.emptyTime
    }
  }

  private def toTimeWithSignedString(value: Option[Float]): String = {
    value match {
      case Some(v) => v.toMinuteFormatFromSecondsWithSigned
      case None => LapTimeDetailsState.emptyTime
    }
  }
}
