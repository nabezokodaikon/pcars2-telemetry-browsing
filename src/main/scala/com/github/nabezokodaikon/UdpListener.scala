package com.github.nabezokodaikon

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging

object UdpListener extends Actor with LazyLogging {

  def receive = {
    case _ =>
      logger.warn("Received unknown message.")
  }
}
