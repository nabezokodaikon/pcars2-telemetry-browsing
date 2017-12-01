package com.github.nabezokodaikon.example.udp

import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.github.nabezokodaikon.ClientManager
import com.github.nabezokodaikon.dataListener.{
  ParticipantsDataListener,
  ParticipantVehicleNamesDataListener,
  VehicleClassNamesDataListener,
  LapTimeDetailsListener,
  FuelDataListener
}
import com.github.nabezokodaikon.udpListener.{
  UdpListener,
  UdpData,
  TelemetryData,
  RaceData,
  ParticipantsData,
  TimingsData,
  GameStateData,
  TimeStatsData,
  ParticipantVehicleNamesData,
  VehicleClassNamesData
}
import com.github.nabezokodaikon.udpListener.UdpDataReader.readUdpData
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging
import java.io.File
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.Exception.catching

object UdpTestDataSender {
  object Received
}

class UdpTestDataSender(clientManager: ActorRef) extends Actor with LazyLogging {
  import UdpTestDataSender._

  case class TestData(path: String, dateTime: Long)

  val regex = """^(\d)(_)(\d+)(\.bin)$""".r
  val srcTestDataList = new File(s"${FileUtil.currentDirectory}/sample/testdata/pcars2").listFiles
    .map {
      f =>
        f.getName match {
          case regex(kind, _, dateTime, ex) if ex == ".bin" /* && kind == "3" */ => TestData(f.getAbsolutePath, dateTime.toLong)
          case _ => TestData("", 0L)
        }
    }
    .filter(_.path.length > 0)
    .sortBy(_.dateTime)
    .toList

  val participantsDataListener = context.actorOf(
    Props(classOf[ParticipantsDataListener], clientManager),
    "ParticipantsDataListener"
  )

  val participantVehicleNamesDataListener = context.actorOf(
    Props(classOf[ParticipantVehicleNamesDataListener], clientManager),
    "ParticipantVehicleNamesDataListener"
  )

  val vehicleClassNamesDataListener = context.actorOf(
    Props(classOf[VehicleClassNamesDataListener], clientManager),
    "VehicleClassNamesDataListener"
  )

  val lapTimeDetailsListener = context.actorOf(
    Props(classOf[LapTimeDetailsListener], clientManager),
    "LapTimeDetailsListener"
  )

  val fuelDataListener = context.actorOf(
    Props(classOf[FuelDataListener], clientManager),
    "FuelDataListener"
  )

  override def preStart() = {
    logger.debug("UdpTestDataSender preStart.");
  }

  override def postStop() = {
    context.children.foreach { child =>
      catching(classOf[AskTimeoutException]).either {
        val stopped = gracefulStop(child, 5.seconds, PoisonPill)
        Await.result(stopped, 6.seconds)
      } match {
        case Left(e) => logger.error(e.getMessage)
        case _ => Unit
      }
    }

    logger.debug("UdpTestDataSender postStop.")
  }

  def receive = {
    case Received =>
      context.become(ready(srcTestDataList, System.currentTimeMillis))
      self ! Received
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }

  def ready(testDataList: List[TestData], previewTime: Long): Receive = {
    case Received =>
      val interval = System.currentTimeMillis - previewTime
      if (interval > 100) {
        testDataList match {
          case head :: Nil =>
            for (udpData <- readUdpData(FileUtil.readBinary(head.path))) sendData(udpData)
            context.become(ready(srcTestDataList, System.currentTimeMillis))
            self ! Received
          case head :: tail =>
            for (udpData <- readUdpData(FileUtil.readBinary(head.path))) sendData(udpData)
            context.become(ready(tail, System.currentTimeMillis))
            self ! Received
          case _ => Unit
        }
      } else {
        Thread.sleep(10)
        context.become(ready(testDataList, previewTime))
        self ! Received
      }
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }

  def sendData(udpData: UdpData): Unit = {
    udpData match {
      case udpData: TelemetryData =>
        clientManager ! udpData
        lapTimeDetailsListener ! udpData
        fuelDataListener ! udpData
      // timeAggregateListener ! udpData
      case udpData: RaceData =>
        clientManager ! udpData
        lapTimeDetailsListener ! udpData
        fuelDataListener ! udpData
      // timeAggregateListener ! udpData
      case udpData: ParticipantsData =>
        participantsDataListener ! udpData
      case udpData: TimingsData =>
        clientManager ! udpData
        lapTimeDetailsListener ! udpData
        fuelDataListener ! udpData
      // timeAggregateListener ! udpData
      case udpData: GameStateData =>
        clientManager ! udpData
        lapTimeDetailsListener ! udpData
        fuelDataListener ! udpData
      // timeAggregateListener ! udpData
      case udpData: TimeStatsData =>
        clientManager ! udpData
        lapTimeDetailsListener ! udpData
      // timeAggregateListener ! udpData
      case udpData: ParticipantVehicleNamesData =>
        participantVehicleNamesDataListener ! udpData
      case udpData: VehicleClassNamesData =>
        vehicleClassNamesDataListener ! udpData
      case _ => Unit
    }
  }
}
