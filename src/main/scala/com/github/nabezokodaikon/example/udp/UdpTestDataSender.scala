package com.github.nabezokodaikon.example.udp

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.{ ClientManager, UdpListener }
import com.github.nabezokodaikon.pcars2.UdpDataReader._
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

  override def preStart() = {
    logger.debug("UdpTestDataSender preStart.");
  }

  override def postStop() = {
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
            readUdpData(FileUtil.readBinary(head.path)) match {
              case Some(udpData) => clientManager ! udpData
              case None => Unit
            }
            context.become(ready(srcTestDataList, System.currentTimeMillis))
            self ! Received
          case head :: tail =>
            readUdpData(FileUtil.readBinary(head.path)) match {
              case Some(udpData) => clientManager ! udpData
              case None => Unit
            }
            context.become(ready(tail, System.currentTimeMillis))
            self ! Received
          case _ => ()
        }
      } else {
        Thread.sleep(10)
        context.become(ready(testDataList, previewTime))
        self ! Received
      }
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }
}
