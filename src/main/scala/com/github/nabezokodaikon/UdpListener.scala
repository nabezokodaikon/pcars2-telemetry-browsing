package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, Terminated }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._

object UdpListener {
  case class OutgoingValue(value: String)
}

// class UdpListener extends Actor with LazyLogging {
// import UdpListener._

// def receive = processing(List[ActorRef]())

// private def processing(clientList: List[ActorRef]): Receive = {
// case AddClient(client) =>
// context.become(processing(clientList :+ client))
// case RemoveClient(client) => {
// try {
// val stopped = gracefulStop(client, 5.seconds, ActorDone)
// Await.result(stopped, 6.seconds)
// } catch {
// case e: AskTimeoutException =>
// logger.error(e.getMessage)
// } finally {
// context.become(processing(clientList.filter(_ != client).toList))
// }
// }
// case ActorDone =>
// println("Done.")
// context.stop(self)
// case _ =>
// logger.warn("Received unknown message.")
// }

// override def postStop = {
// println("UdpListener stop.")
// }
// }
