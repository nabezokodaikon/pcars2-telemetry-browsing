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

  case class TestData(path: String, dateTime: Long)

  val regex = """^(\d)(_)(\d+)(\.bin)$""".r
  val srcTestDataList = new File(s"${FileUtil.currentDirectory}/testdata/pcars1").listFiles
    .map {
      f =>
        f.getName match {
          case regex(_, _, dateTime, ex) if ex == ".bin" => TestData(f.getAbsolutePath, dateTime.toLong)
          case _ => TestData("", 0L)
        }
    }
    .sortBy(_.dateTime)
    .toList

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
            clientManager ! UdpListener.OutgoingValue(getJsonText(FileUtil.readBinary(head.path)))
            context.become(ready(srcTestDataList, System.currentTimeMillis))
            self ! Received
          case head :: tail =>
            clientManager ! UdpListener.OutgoingValue(getJsonText(FileUtil.readBinary(head.path)))
            context.become(ready(tail, System.currentTimeMillis))
            self ! Received
          case _ => ()
        }
      } else {
        Thread.sleep(10)
        context.become(ready(testDataList, previewTime))
        self ! Received
      }
    case ActorDone =>
      println("UdpTestDataSender Done.")
      context.stop(self)
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }
}
