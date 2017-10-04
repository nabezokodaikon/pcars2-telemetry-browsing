package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging

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
    case RemoveClient(client) =>
      context.become(processing(clientList.filter(_ != client).toList))
    case value: UdpListener.OutgoingValue =>
      clientList.foreach(_ ! value)
    case ActorDone =>
      println("ClientManager Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  override def postStop() = {
    println("ClientManager stop.")
  }
}
