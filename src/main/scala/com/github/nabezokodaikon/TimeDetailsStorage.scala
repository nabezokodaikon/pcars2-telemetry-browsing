package com.github.nabezokodaikon

import akka.actor.{ Actor }
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.pcars2.{
  TelemetryData,
  TimingsData,
  TimeStatsData,
  LapTimeDetails,
  TimeDetails
}
import com.typesafe.scalalogging.LazyLogging

case class CurrentData(
    currentLap: Short,
    sector: Short
)

case class LapData(
    lap: Short,
    sector1: Option[Float],
    sector2: Option[Float],
    sector3: Option[Float],
    lapTime: Option[Float]
) {
  def toLapTimeDetails(): LapTimeDetails = {
    LapTimeDetails(
      lap = lap,
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

case class History(
    viewedParticipantIndex: Byte,
    currentData: Option[CurrentData],
    currentLapData: Option[LapData],
    lapDataList: List[LapData]
) {
  // TODO
  // def toTimeDetails(): TimeDetails = {
  // }
}

final class TimeDetailsStorage(clientManager: ClientManager)
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug("TimeDetailsStorage preStart.");
  }

  override def postStop() = {
    logger.debug("TimeDetailsStorage postStop.")
  }

  private def createHistory(viewedParticipantIndex: Byte) = {
    History(viewedParticipantIndex, None, None, List[LapData]())
  }

  private def mergeHistory(history: History, currentData: CurrentData): History = {
    History(
      history.viewedParticipantIndex,
      Some(currentData),
      history.currentLapData,
      history.lapDataList
    )
  }

  private def mergeHistory(history: History, currentLapData: LapData): History = {
    History(
      history.viewedParticipantIndex,
      history.currentData,
      Some(currentLapData),
      history.lapDataList
    )
  }

  private def addHistory(history: History, currentLapData: LapData): History = {
    History(
      history.viewedParticipantIndex,
      history.currentData,
      None,
      history.lapDataList :+ currentLapData
    )
  }

  def receive(): Receive = {
    case udpData: TelemetryData =>
      context.become(processing(createHistory(udpData.participantinfo.viewedParticipantIndex)))
    case _ => Unit
  }

  def processing(history: History): Receive = {
    case udpData: TelemetryData if udpData.participantinfo.viewedParticipantIndex != history.viewedParticipantIndex =>
      val nextHistory = createHistory(udpData.participantinfo.viewedParticipantIndex)
      context.become(processing(nextHistory))
    case udpData: TimingsData =>
      history.currentData match {
        case Some(currentData) =>
          val participant = udpData.partcipants(history.viewedParticipantIndex)
          if (participant.currentLap != currentData.currentLap
            || participant.sector != currentData.sector) {
            val newCurrentData = CurrentData(participant.currentLap, participant.sector)
            val nextHistory = mergeHistory(history, newCurrentData)
            context.become(processing(nextHistory))
          }
        case None =>
          val participant = udpData.partcipants(history.viewedParticipantIndex)
          val newCurrentData = CurrentData(participant.currentLap, participant.sector)
          val nextHistory = mergeHistory(history, newCurrentData)
          context.become(processing(nextHistory))
      }
    case udpData: TimeStatsData =>
      (history.currentData, history.currentLapData) match {
        case (Some(currentData), Some(currentLapData)) =>
          currentData.currentLap match {
            case currentLap if currentLap == currentLapData.lap =>
              currentData.sector match {
                case 1 =>
                  val lapData = LapData(currentData.currentLap, None, None, None, None)
                  val nextHistory = mergeHistory(history, lapData)
                  context.become(processing(nextHistory))
                case 2 =>
                  val stat = udpData.stats.participants(history.viewedParticipantIndex)
                  val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
                  val nextHistory = mergeHistory(history, lapData)
                  context.become(processing(nextHistory))
                case 3 =>
                  val stat = udpData.stats.participants(history.viewedParticipantIndex)
                  val lapData = LapData(currentData.currentLap, currentLapData.sector1, Some(stat.lastSectorTime), None, None)
                  val nextHistory = mergeHistory(history, lapData)
                  context.become(processing(nextHistory))
                case _ =>
                  logger.warn(s"Received unknown sector: ${currentData.sector}")
                  val nextHistory = createHistory(history.viewedParticipantIndex)
                  context.become(processing(nextHistory))
              }
            case currentLap if currentLap > currentLapData.lap =>
              (currentLapData.sector1, currentLapData.sector2, currentLapData.sector3) match {
                case (Some(_), Some(_), None) =>
                  val stat = udpData.stats.participants(history.viewedParticipantIndex)
                  val lapData = LapData(
                    currentLapData.lap, currentLapData.sector1, currentLapData.sector2, Some(stat.lastSectorTime), Some(stat.lastLapTime)
                  )
                  val nextHistory = addHistory(history, lapData)
                  context.become(processing(nextHistory))
                case (Some(_), None, None) =>
                  val stat = udpData.stats.participants(history.viewedParticipantIndex)
                  val lapData = LapData(
                    currentLapData.lap, currentLapData.sector1, Some(stat.lastSectorTime), None, Some(stat.lastLapTime)
                  )
                  val nextHistory = addHistory(history, lapData)
                  context.become(processing(nextHistory))
                case (None, None, None) =>
                  val stat = udpData.stats.participants(history.viewedParticipantIndex)
                  val lapData = LapData(
                    currentLapData.lap, Some(stat.lastSectorTime), None, None, Some(stat.lastLapTime)
                  )
                  val nextHistory = addHistory(history, lapData)
                  context.become(processing(nextHistory))
                case _ =>
                  val nextHistory = createHistory(history.viewedParticipantIndex)
                  context.become(processing(nextHistory))
              }
            case _ =>
              val nextHistory = createHistory(history.viewedParticipantIndex)
              context.become(processing(nextHistory))
          }
        case (Some(currentData), None) =>
          currentData.sector match {
            case 1 =>
              val lapData = LapData(currentData.currentLap, None, None, None, None)
              val nextHistory = mergeHistory(history, lapData)
              context.become(processing(nextHistory))
            case 2 =>
              val stat = udpData.stats.participants(history.viewedParticipantIndex)
              val lapData = LapData(currentData.currentLap, Some(stat.lastSectorTime), None, None, None)
              val nextHistory = mergeHistory(history, lapData)
              context.become(processing(nextHistory))
            case 3 =>
              val stat = udpData.stats.participants(history.viewedParticipantIndex)
              val lapData = LapData(currentData.currentLap, None, Some(stat.lastSectorTime), None, None)
              val nextHistory = mergeHistory(history, lapData)
              context.become(processing(nextHistory))
            case _ =>
              logger.warn(s"Received unknown sector: ${currentData.sector}")
              val nextHistory = createHistory(history.viewedParticipantIndex)
              context.become(processing(nextHistory))
          }
        case _ => Unit
      }
  }
}
