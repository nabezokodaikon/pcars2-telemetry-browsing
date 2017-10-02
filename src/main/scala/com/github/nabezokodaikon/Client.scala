package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, Props }
import akka.pattern.{ AskTimeoutException, ask }
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

object Client {
  case class AddClient(client: ActorRef)
  case class RemoveClient(client: ActorRef)
  case class Connected(outgoing: ActorRef)
  case class OutgoingValue(value: String)
}

class HttpClientManager {
}

class Client(udpListener: ActorRef) extends Actor with LazyLogging {
  import Client._

  def receive = {
    case Connected(outgoing) =>
      udpListener ! UdpListener.AddClient(self)
      context.become(processing(outgoing))
    case _ =>
      logger.warn("Received unknown message.")
  }

  private def processing(outgoing: ActorRef): Receive = {
    case OutgoingValue(value) =>
      val f = (outgoing ? value)(5.seconds).mapTo[String]
      f.onComplete {
        case Success(_) => Unit
        case Failure(e) =>
          logger.info(e.getMessage)
          udpListener ! UdpListener.RemoveClient(self)
      }
    case ActorDone =>
      println("Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }
}
