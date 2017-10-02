package com.github.nabezokodaikon

import akka.actor.{ Actor, Terminated }
import com.typesafe.scalalogging.LazyLogging

class UdpListener extends Actor with LazyLogging {

  def receive = processing(Seq[String]())

  private def processing(clients: Seq[String]): Receive = {
    case name: String =>
      // println(name)
      val next = clients :+ s"Hello ${name}!"
      val msg = next.mkString
      sender ! msg
      context.become(processing(next))
    case ActorDone =>
      println("Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  override def postStop = {
    println("postStop")
  }
}
