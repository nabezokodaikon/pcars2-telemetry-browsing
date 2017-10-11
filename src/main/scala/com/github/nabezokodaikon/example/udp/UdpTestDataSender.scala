package com.github.nabezokodaikon.example.udp

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.{ ActorDone, ClientManager, UdpListener }
import com.typesafe.scalalogging.LazyLogging

object UdpTestDataSender {
  object Received
}

class UdpTestDataSender(clientManager: ActorRef) extends Actor with LazyLogging {
  import UdpTestDataSender._

  val srcTestData = Seq[String]("test01", "test02", "test03")

  def receive = {
    case Received =>
      context.become(ready(srcTestData, System.currentTimeMillis))
      self ! Received
    case _ =>
      logger.warn("UdpTestDataSender received unknown message.")
  }

  def ready(testData: Seq[String], previewTime: Long): Receive = {
    case Received =>
      val interval = System.currentTimeMillis - previewTime
      if (interval > 1000) {
        testData match {
          case head :: Nil =>
            clientManager ! UdpListener.OutgoingValue(head)
            context.become(ready(srcTestData, System.currentTimeMillis))
            self ! Received
          case head :: tail =>
            clientManager ! UdpListener.OutgoingValue(head)
            context.become(ready(tail, System.currentTimeMillis))
            self ! Received
        }
      } else {
        Thread.sleep(100)
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
