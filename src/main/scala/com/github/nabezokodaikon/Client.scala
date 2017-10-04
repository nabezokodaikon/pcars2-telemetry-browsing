package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._

object ClientManager {
  case class AddClient(client: ActorRef)
  case class RemoveClient(client: ActorRef)
}

class ClientManager extends Actor with LazyLogging {
  import ClientManager._

  def receive() = processing(List[ActorRef]())

  private def processing(clientList: List[ActorRef]): Receive = {
    case AddClient(client) =>
      context.become(processing(clientList :+ client))
    case RemoveClient(client) => {
      try {
        val stopped = gracefulStop(client, 5.seconds, ActorDone)
        Await.result(stopped, 6.seconds)
      } catch {
        case e: AskTimeoutException =>
          logger.error(e.getMessage)
      } finally {
        context.become(processing(clientList.filter(_ != client).toList))
      }
    }
    case value: UdpListener.OutgoingValue =>
      clientList.foreach(_ ! value)
    case ActorDone =>
      println("ClientManager Done.")
      clientList.foreach { client =>
        try {
          val stopped = gracefulStop(client, 5.seconds, ActorDone)
          Await.result(stopped, 6.seconds)
        } catch {
          case e: AskTimeoutException =>
            logger.error(e.getMessage)
        }
      }
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  override def postStop() = {
    println("ClientManager stop.")
  }
}

object Client {
  case class Connected(outgoing: ActorRef)
}

class Client(manager: ActorRef) extends Actor with LazyLogging {
  import Client._

  def receive() = {
    case Connected(outgoing: ActorRef) =>
      context.become(processing(outgoing))
  }

  private def processing(outgoing: ActorRef): Receive = {
    case value: UdpListener.OutgoingValue =>
      outgoing ! value
    case ActorDone =>
      println("Client Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  override def postStop() = {
    println("Client stop.")
  }
}
