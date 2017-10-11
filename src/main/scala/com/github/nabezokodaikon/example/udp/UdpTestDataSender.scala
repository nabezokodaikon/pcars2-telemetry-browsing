package com.github.nabezokodaikon.example.udp

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.{ ActorDone, ClientManager, UdpListener }
import com.github.nabezokodaikon.pcars1.TelemetryDataConst._
import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory._
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging
import java.io.File

object UdpTestDataSender {
  object Received
}

class UdpTestDataSender(clientManager: ActorRef) extends Actor with LazyLogging {
  import UdpTestDataSender._

  case class TestData(path: String, dateTime: Long) {
    val data = if (path != "") FileUtil.readBinary(path).toList else List[Byte]()
    val frameType = if (path != "") createFrameInfo(data.toList).frameType else 9
  }

  val regex = """^(\d)(_)(\d+)(\.bin)$""".r
  val srcTestData = new File(s"${FileUtil.getCurrentDirectory}/testdata").listFiles
    .map {
      f =>
        f.getName match {
          case regex(_, _, dateTime, ex) if ex == ".bin" => TestData(f.getAbsolutePath, dateTime.toLong)
          case _ => TestData("", 0L)
        }
    }
    .sortBy(_.dateTime)
    .toList

  def getDataText(testData: TestData) =
    testData.frameType match {
      case TELEMETRY_DATA_FRAME_TYPE => createTelemetryData(testData.data).toJsonString
      case PARTICIPANT_INFO_STRINGS_FRAME_TYPE => createParticipantInfoStrings(testData.data).toJsonString
      case PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE => ""
      case _ => ""
    }

  def receive = {
    case Received =>
      context.become(ready(srcTestData, System.currentTimeMillis))
      self ! Received
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }

  def ready(testData: List[TestData], previewTime: Long): Receive = {
    case Received =>
      val interval = System.currentTimeMillis - previewTime
      if (interval > 100) {
        testData match {
          case head :: Nil =>
            clientManager ! UdpListener.OutgoingValue(getDataText(head))
            context.become(ready(srcTestData, System.currentTimeMillis))
            self ! Received
          case head :: tail =>
            clientManager ! UdpListener.OutgoingValue(getDataText(head))
            context.become(ready(tail, System.currentTimeMillis))
            self ! Received
          case _ => ()
        }
      } else {
        Thread.sleep(10)
        context.become(ready(testData, previewTime))
        self ! Received
      }
    case ActorDone =>
      println("UdpTestDataSender Done.")
      context.stop(self)
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }
}
