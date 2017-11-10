package com.github.nabezokodaikon.dataListener

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  TelemetryData,
  RaceData,
  TimingsData,
  TimeStatsData,
  LapTime,
  LapTimeDetails
}
import com.typesafe.scalalogging.LazyLogging

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
      lapTime = toTimeString(lapTime)
    )
  }

  private def toTimeString(value: Option[Float]): String = {
    value match {
      case Some(v) => v.toMinuteFormatFromSeconds
      case None => "--:--.---"
    }
  }
}

final object History {
  val emptyLap = "-"
  val emptyTime = "--:--.---"
  val emptyLapTime = LapTime(
    emptyLap,
    emptyTime,
    emptyTime,
    emptyTime,
    emptyTime
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
}

final case class History(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    isTimedSessions: Boolean,
    lapsInEvent: Int,
    viewedParticipantIndex: Byte,
    currentView: Option[LapData],
    currentData: Option[CurrentData],
    currentLapData: Option[LapData],
    lapDataList: List[LapData]
) {
  import History._

  def toLapTimeDetails(): LapTimeDetails = {
    val (current, lap) = currentLapData match {
      case Some(v) => (v.toLapTime, v.lap)
      case None => (emptyLapTime, 0.toShort)
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
          emptyLap,
          sector1 = averageSector1.toMinuteFormatFromSeconds,
          sector2 = averageSector2.toMinuteFormatFromSeconds,
          sector3 = averageSector3.toMinuteFormatFromSeconds,
          lapTime = averageLapTime.toMinuteFormatFromSeconds
        )

        LapTimeDetails(
          base = base,
          isTimedSessions = isTimedSessions,
          lapsInEvent = lapsInEvent,
          current = current,
          fastest = fastest,
          average = average,
          lapDataList.takeRight(3).map(_.toLapTime)
        )
    }
  }
}

final class LapTimeDetailsListener(clientManager: ActorRef)
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug("LapTimeDetailsListener preStart.");
  }

  override def postStop() = {
    logger.debug("LapTimeDetailsListener postStop.")
  }

  def receive(): Receive = {
    case udpData: GameStateData =>
      context.become(processing(createInitialHistory(udpData)))
  }

  private def processing(history: History): Receive = {
    case udpData: GameStateData =>
      val nextHistory = resetHistoryByGameStateData(history, udpData)
      context.become(processing(nextHistory))
    case udpData: RaceData if (history.isMenu) =>
      val nextHistory = resetHistoryByRaceData(history, udpData)
      val lapTimeDetails = nextHistory.toLapTimeDetails
      clientManager ! lapTimeDetails
      context.become(processing(nextHistory))
    case udpData: TelemetryData if (history.isPlaying) =>
      if (udpData.participantinfo.viewedParticipantIndex != history.viewedParticipantIndex) {
        val nextHistory = resetHistoryByViewedParticipantIndex(history, udpData.participantinfo.viewedParticipantIndex)
        context.become(processing(nextHistory))
      }
    case udpData: TimingsData if (history.isMenu && history.currentData != None) =>
      val nextHistory = resetHistory(history)
      context.become(processing(nextHistory))
    case udpData: TimingsData if (history.isRestart && history.currentData != None) =>
      val nextHistory = resetHistory(history)
      context.become(processing(nextHistory))
    case udpData: TimingsData if (history.isPlaying) =>
      // TODO: 時間制限のレースでセッション時間のバグがなくなったら、最終ラップを現在のラップのままにする。
      if (history.isTimedSessions || history.lapsInEvent > history.lapDataList.length) {
        createHistory(history, udpData) match {
          case Some(nextHistory) =>
            val lapTimeDetails = nextHistory.toLapTimeDetails
            context.become(processing(nextHistory))
          case None => Unit
        }
      }
    case udpData: TimeStatsData if (history.isMenu) =>
      val nextHistory = resetHistory(history)
      val lapTimeDetails = nextHistory.toLapTimeDetails
      clientManager ! lapTimeDetails
      context.become(processing(nextHistory))
    case udpData: TimeStatsData if (history.isRestart) =>
      val nextHistory = resetHistory(history)
      val lapTimeDetails = nextHistory.toLapTimeDetails
      clientManager ! lapTimeDetails
      context.become(processing(nextHistory))
    case udpData: TimeStatsData if (history.isPlaying) =>
      // TODO: 時間制限のレースでセッション時間のバグがなくなったら、最終ラップを現在のラップのままにする。
      if (history.isTimedSessions || history.lapsInEvent > history.lapDataList.length) {
        createHistory(history, udpData) match {
          case Some(nextHistory) =>
            val lapTimeDetails = nextHistory.toLapTimeDetails
            clientManager ! lapTimeDetails
            context.become(processing(nextHistory))
          case None => Unit
        }
      }
  }

  private def createInitialHistory(gameStateData: GameStateData): History =
    History(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      isTimedSessions = false,
      lapsInEvent = 0,
      viewedParticipantIndex = 0,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  private def resetHistoryByGameStateData(history: History, gameStateData: GameStateData): History =
    History(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = history.viewedParticipantIndex,
      currentView = history.currentView,
      currentData = history.currentData,
      currentLapData = history.currentLapData,
      lapDataList = history.lapDataList
    )

  private def resetHistoryByRaceData(history: History, raceData: RaceData): History =
    History(
      isMenu = history.isMenu,
      isPlaying = history.isPlaying,
      isRestart = history.isRestart,
      isTimedSessions = raceData.isTimedSessions,
      lapsInEvent = raceData.lapsInEvent,
      viewedParticipantIndex = history.viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  private def resetHistoryByViewedParticipantIndex(history: History, viewedParticipantIndex: Byte): History =
    History(
      isMenu = history.isMenu,
      isPlaying = history.isPlaying,
      isRestart = history.isRestart,
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  private def resetHistory(history: History): History =
    History(
      isMenu = history.isMenu,
      isPlaying = history.isPlaying,
      isRestart = history.isRestart,
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = history.viewedParticipantIndex,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  private def createHistory(history: History, timingsData: TimingsData): Option[History] =
    history.currentData match {
      case Some(currentData) =>
        val participant = timingsData.partcipants(history.viewedParticipantIndex)
        if (participant.currentLap != currentData.currentLap
          || participant.sector != currentData.sector) {
          val newCurrentData = CurrentData(
            participant.currentLap, participant.sector,
            participant.currentTime, participant.currentSectorTime
          )
          Some(mergeHistory(history, newCurrentData))
        } else {
          None
        }
      case None =>
        val participant = timingsData.partcipants(history.viewedParticipantIndex)
        val newCurrentData = CurrentData(
          participant.currentLap, participant.sector,
          participant.currentTime, participant.currentSectorTime
        )
        Some(mergeHistory(history, newCurrentData))
    }

  private def createHistory(history: History, timeStatsData: TimeStatsData): Option[History] =
    (history.currentData, history.currentLapData) match {
      case (Some(currentData), Some(currentLapData)) =>
        currentData.currentLap match {
          case currentLap if (currentLap == currentLapData.lap) =>
            currentData.sector match {
              case 1 =>
                val lapData = LapData(currentData.currentLap, None, None, None, None)
                Some(mergeHistory(history, lapData))
              case 2 =>
                val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
                Some(mergeHistory(history, lapData))
              case 3 =>
                val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, currentLapData.sector1, Some(stat.lastSectorTime), None, None)
                Some(mergeHistory(history, lapData))
              case _ =>
                logger.warn(s"Received unknown sector: ${currentData.sector}")
                Some(resetHistory(history))
            }
          case currentLap if (currentLap > currentLapData.lap) =>
            (currentLapData.sector1, currentLapData.sector2, currentLapData.sector3) match {
              case (Some(_), Some(_), Some(_)) =>
                Some(history)
              case (Some(_), Some(_), None) =>
                val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, currentLapData.sector2, Some(stat.lastSectorTime), Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case (Some(_), None, None) =>
                val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, Some(stat.lastSectorTime), None, Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case (None, None, None) =>
                val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, Some(stat.lastSectorTime), None, None, Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case _ =>
                logger.warn("Current data is inconsistent.")
                Some(resetHistory(history))
            }
          case _ =>
            logger.warn("Current lap is inconsistent.")
            Some(resetHistory(history))
        }
      case (Some(currentData), None) =>
        currentData.sector match {
          case 1 =>
            val lapData = LapData(currentData.currentLap, None, None, None, None)
            Some(mergeHistory(history, lapData))
          case 2 =>
            val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
            Some(mergeHistory(history, lapData))
          case 3 =>
            val stat = timeStatsData.stats.participants(history.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, None, Some(stat.lastSectorTime), None, None)
            Some(mergeHistory(history, lapData))
          case _ =>
            logger.warn(s"Received unknown sector: ${currentData.sector}")
            Some(resetHistory(history))
        }
      case _ => None
    }

  private def mergeHistory(history: History, currentData: CurrentData): History = {
    val currentView = history.currentLapData match {
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

    History(
      isMenu = history.isMenu,
      isPlaying = history.isPlaying,
      isRestart = history.isRestart,
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = history.viewedParticipantIndex,
      currentView = currentView,
      currentData = Some(currentData),
      currentLapData = history.currentLapData,
      lapDataList = history.lapDataList
    )
  }

  private def mergeHistory(history: History, currentLapData: LapData): History =
    History(
      isMenu = history.isMenu,
      isPlaying = history.isPlaying,
      isRestart = history.isRestart,
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = history.viewedParticipantIndex,
      currentView = history.currentView,
      currentData = history.currentData,
      currentLapData = Some(currentLapData),
      lapDataList = history.lapDataList
    )

  private def addHistory(history: History, currentLapData: LapData): History =
    history.isTimedSessions match {
      case true =>
        History(
          isMenu = history.isMenu,
          isPlaying = history.isPlaying,
          isRestart = history.isRestart,
          isTimedSessions = history.isTimedSessions,
          lapsInEvent = history.lapsInEvent,
          viewedParticipantIndex = history.viewedParticipantIndex,
          currentView = history.currentView,
          currentData = history.currentData,
          currentLapData = None,
          lapDataList = history.lapDataList :+ currentLapData
        )
      case false if ((history.lapsInEvent - 1) > history.lapDataList.length) =>
        History(
          isMenu = history.isMenu,
          isPlaying = history.isPlaying,
          isRestart = history.isRestart,
          isTimedSessions = history.isTimedSessions,
          lapsInEvent = history.lapsInEvent,
          viewedParticipantIndex = history.viewedParticipantIndex,
          currentView = history.currentView,
          currentData = history.currentData,
          currentLapData = None,
          lapDataList = history.lapDataList :+ currentLapData
        )
      case false if (history.lapsInEvent > history.lapDataList.length) =>
        History(
          isMenu = history.isMenu,
          isPlaying = history.isPlaying,
          isRestart = history.isRestart,
          isTimedSessions = history.isTimedSessions,
          lapsInEvent = history.lapsInEvent,
          viewedParticipantIndex = history.viewedParticipantIndex,
          currentView = history.currentView,
          currentData = history.currentData,
          currentLapData = Some(currentLapData),
          lapDataList = history.lapDataList
        )
      case _ =>
        history
    }
}
