package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.pcars2.UdpData
import com.typesafe.scalalogging.LazyLogging

object ClientManager {
  case class AddClient(client: ActorRef)
  case class RemoveClient(client: ActorRef)
}

class ClientManager extends Actor with LazyLogging {
  import ClientManager._

  override def preStart() = {
    logger.debug("ClientManager preStart.");
  }

  override def postStop() = {
    logger.debug("ClientManager postStop.")
  }

  def receive() = processing(List[ActorRef]())

  private def processing(clientList: List[ActorRef]): Receive = {
    case AddClient(client) =>
      context.become(processing(clientList :+ client))
    case RemoveClient(client) =>
      context.become(processing(clientList.filter(_ != client).toList))
    case udpData: UdpData =>
      val json = udpData.toJsonString
      clientList.foreach(_ ! json)
    case _ =>
      logger.warn("ClientManager received unknown message.")
  }
}
