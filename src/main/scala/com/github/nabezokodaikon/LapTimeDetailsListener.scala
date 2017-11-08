package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  UdpStreamerPacketHandlerType,
  PacketBase,
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

final case class History(
    isTimedSessions: Boolean,
    lapsInEvent: Int,
    viewedParticipantIndex: Byte,
    currentView: Option[LapData],
    currentData: Option[CurrentData],
    currentLapData: Option[LapData],
    lapDataList: List[LapData]
) {
  private val emptyLap = "-"
  private val emptyTime = "--:--.---"
  private val emptyLapTime = LapTime(
    emptyLap,
    emptyTime,
    emptyTime,
    emptyTime,
    emptyTime
  )

  private val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.LAP_TIME_DETAILS,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )

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
    case udpData: RaceData =>
      context.become(processing(createInitialHistory(udpData)))
    case udpData: GameStateData => Unit
      // TODO: ここかprocessingかタイミングを確認する。 
    case _ => Unit
  }

  private def processing(history: History): Receive = {
    case udpData: TelemetryData if (udpData.participantinfo.viewedParticipantIndex != history.viewedParticipantIndex) =>
      val nextHistory = createInitialHistory(history, udpData.participantinfo.viewedParticipantIndex)
      context.become(processing(nextHistory))
    case udpData: TimingsData if (history.isTimedSessions || history.lapsInEvent > history.lapDataList.length) =>
      createHistory(history, udpData) match {
        case Some(nextHistory) =>
          context.become(processing(nextHistory))
        case None => Unit
      }
    case udpData: TimeStatsData if (history.isTimedSessions || history.lapsInEvent > history.lapDataList.length) =>
      createHistory(history, udpData) match {
        case Some(nextHistory) =>
          val lapTimeDetails = nextHistory.toLapTimeDetails
          clientManager ! lapTimeDetails
          context.become(processing(nextHistory))
        case None => Unit
      }
    case udpData: RaceData =>
      val nextHistory = createInitialHistory(udpData)
      val lapTimeDetails = nextHistory.toLapTimeDetails
      clientManager ! lapTimeDetails
      context.become(processing(nextHistory))
  }

  private def createInitialHistory(raceData: RaceData): History =
    History(
      isTimedSessions = raceData.isTimedSessions,
      lapsInEvent = raceData.lapsInEvent,
      viewedParticipantIndex = 0,
      currentView = None,
      currentData = None,
      currentLapData = None,
      lapDataList = List[LapData]()
    )

  private def createInitialHistory(history: History, viewedParticipantIndex: Byte): History =
    History(
      isTimedSessions = history.isTimedSessions,
      lapsInEvent = history.lapsInEvent,
      viewedParticipantIndex = viewedParticipantIndex,
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
                Some(createInitialHistory(history, history.viewedParticipantIndex))
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
                Some(createInitialHistory(history, history.viewedParticipantIndex))
            }
          case _ =>
            logger.warn("Current lap is inconsistent.")
            Some(createInitialHistory(history, history.viewedParticipantIndex))
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
            Some(createInitialHistory(history, history.viewedParticipantIndex))
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
