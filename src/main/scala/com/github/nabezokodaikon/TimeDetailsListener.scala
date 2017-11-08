package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  UdpStreamerPacketHandlerType,
  PacketBase,
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
      lap = s"${"%03d".format(lap)}",
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
    viewedParticipantIndex: Byte,
    currentView: Option[LapData],
    currentData: Option[CurrentData],
    currentLapData: Option[LapData],
    lapDataList: List[LapData]
) {
  private val emptyLap = "---"
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
    packetType = UdpStreamerPacketHandlerType.TIME_DETAILS,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )

  def toTimeDetails(): LapTimeDetails = {
    val current = currentLapData match {
      case Some(v) => v.toLapTime
      case None => emptyLapTime
    }

    lapDataList.length match {
      case 0 =>
        LapTimeDetails(
          base = base,
          current = current,
          fastest = emptyLapTime,
          average = emptyLapTime,
          history = List[LapTime]()
        )
      case _ =>
        val fastestLapTime = lapDataList.flatMap(_.lapTime).min
        val fastest = lapDataList.find(lapData => lapData.lapTime match {
          case Some(lapTime) if lapTime == fastestLapTime => true
          case None => false
        }) match {
          case Some(lapData) => lapData.toLapTime
          case None => emptyLapTime
        }

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
          current = current,
          fastest = fastest,
          average = average,
          lapDataList.drop(4).take(3).map(_.toLapTime)
        )
    }
  }
}

final class TimeDetailsListener(clientManager: ActorRef)
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug("TimeDetailsStorage preStart.");
  }

  override def postStop() = {
    logger.debug("TimeDetailsStorage postStop.")
  }

  def receive(): Receive = {
    case udpData: TelemetryData =>
      context.become(processing(createHistory(udpData.participantinfo.viewedParticipantIndex)))
    case _ => Unit
  }

  private def processing(history: History): Receive = {
    case udpData: TelemetryData if udpData.participantinfo.viewedParticipantIndex != history.viewedParticipantIndex =>
      val nextHistory = createHistory(udpData.participantinfo.viewedParticipantIndex)
      context.become(processing(nextHistory))
    case udpData: TimingsData =>
      createHistory(udpData, history) match {
        case Some(nextHistory) =>
          context.become(processing(nextHistory))
        case None => Unit
      }
    case udpData: TimeStatsData =>
      createHistory(udpData, history) match {
        case Some(nextHistory) =>
          val timeDetails = nextHistory.toTimeDetails
          clientManager ! timeDetails
          context.become(processing(nextHistory))
        case None => Unit
      }
    case _ =>
      logger.warn("TimeDetailsListener received unknown message.")
  }

  private def createHistory(viewedParticipantIndex: Byte): History =
    History(viewedParticipantIndex, None, None, None, List[LapData]())

  private def createHistory(udpData: TimingsData, history: History): Option[History] =
    history.currentData match {
      case Some(currentData) =>
        val participant = udpData.partcipants(history.viewedParticipantIndex)
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
        val participant = udpData.partcipants(history.viewedParticipantIndex)
        val newCurrentData = CurrentData(
          participant.currentLap, participant.sector,
          participant.currentTime, participant.currentSectorTime
        )
        Some(mergeHistory(history, newCurrentData))
    }

  private def createHistory(udpData: TimeStatsData, history: History): Option[History] =
    (history.currentData, history.currentLapData) match {
      case (Some(currentData), Some(currentLapData)) =>
        currentData.currentLap match {
          case currentLap if currentLap == currentLapData.lap =>
            currentData.sector match {
              case 1 =>
                val lapData = LapData(currentData.currentLap, None, None, None, None)
                Some(mergeHistory(history, lapData))
              case 2 =>
                val stat = udpData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
                Some(mergeHistory(history, lapData))
              case 3 =>
                val stat = udpData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(currentData.currentLap, currentLapData.sector1, Some(stat.lastSectorTime), None, None)
                Some(mergeHistory(history, lapData))
              case _ =>
                logger.warn(s"Received unknown sector: ${currentData.sector}")
                Some(createHistory(history.viewedParticipantIndex))
            }
          case currentLap if currentLap > currentLapData.lap =>
            (currentLapData.sector1, currentLapData.sector2, currentLapData.sector3) match {
              case (Some(_), Some(_), None) =>
                val stat = udpData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, currentLapData.sector2, Some(stat.lastSectorTime), Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case (Some(_), None, None) =>
                val stat = udpData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, currentLapData.sector1, Some(stat.lastSectorTime), None, Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case (None, None, None) =>
                val stat = udpData.stats.participants(history.viewedParticipantIndex)
                val lapData = LapData(
                  currentLapData.lap, Some(stat.lastSectorTime), None, None, Some(stat.lastLapTime)
                )
                Some(addHistory(history, lapData))
              case _ =>
                Some(createHistory(history.viewedParticipantIndex))
            }
          case _ =>
            Some(createHistory(history.viewedParticipantIndex))
        }
      case (Some(currentData), None) =>
        currentData.sector match {
          case 1 =>
            val lapData = LapData(currentData.currentLap, None, None, None, None)
            Some(mergeHistory(history, lapData))
          case 2 =>
            val stat = udpData.stats.participants(history.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
            Some(mergeHistory(history, lapData))
          case 3 =>
            val stat = udpData.stats.participants(history.viewedParticipantIndex)
            val lapData = LapData(currentData.currentLap, None, Some(stat.lastSectorTime), None, None)
            Some(mergeHistory(history, lapData))
          case _ =>
            logger.warn(s"Received unknown sector: ${currentData.sector}")
            Some(createHistory(history.viewedParticipantIndex))
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
      history.viewedParticipantIndex,
      currentView,
      Some(currentData),
      history.currentLapData,
      history.lapDataList
    )
  }

  private def mergeHistory(history: History, currentLapData: LapData): History =
    History(
      history.viewedParticipantIndex,
      history.currentView,
      history.currentData,
      Some(currentLapData),
      history.lapDataList
    )

  private def addHistory(history: History, currentLapData: LapData): History =
    History(
      history.viewedParticipantIndex,
      history.currentView,
      history.currentData,
      None,
      history.lapDataList :+ currentLapData
    )
}
