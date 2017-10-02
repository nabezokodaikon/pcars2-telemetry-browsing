package com.github.nabezokodaikon

import akka.actor.{ Actor, Terminated }
import com.typesafe.scalalogging.LazyLogging

class UdpListener extends Actor with LazyLogging {

  def receive = {
    case name: String =>
      println(name)
      sender ! s"Hello ${name}!"
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
