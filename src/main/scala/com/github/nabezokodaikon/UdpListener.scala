package com.github.nabezokodaikon

import akka.actor.{ Actor, Terminated }
import com.typesafe.scalalogging.LazyLogging

class UdpListener extends Actor with LazyLogging {

  def receive = {
    case text: String =>
      println(text)
    case ActorDone =>
      println("Done.")
      context.stop(self)
    case _ =>
      println
      logger.warn("Received unknown message.")
  }

  override def postStop = {
    println("postStop")
  }
}
