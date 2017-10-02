package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging

object UdpListener {
  case class OutgoingValue(value: String)
}

class UdpListener(clientManager: ActorRef) extends Actor with LazyLogging {
  import UdpListener._

  def receive() = {
    case test: String =>
      clientManager ! OutgoingValue(test)
    case ActorDone =>
      println("UdpListener Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  override def postStop() = {
    println("UdpListener stop.")
  }
}
