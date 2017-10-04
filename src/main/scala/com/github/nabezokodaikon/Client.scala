package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, Props }
import akka.pattern.{ AskTimeoutException, ask, gracefulStop }
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

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
    case Connected(outgoing: ActorRef) =>
      context.become(processing(outgoing))
    case value: UdpListener.OutgoingValue =>
      val f = (outgoing ? value)(5.seconds).mapTo[UdpListener.OutgoingValue]
      f.onComplete {
        case Success(_) => Unit
        case Failure(e) =>
          logger.error(e.getMessage)
          manager ! ClientManager.RemoveClient(self)
      }
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
