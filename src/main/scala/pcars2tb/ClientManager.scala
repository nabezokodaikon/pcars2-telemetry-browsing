package pcars2tb

import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.listener.{ UdpData, UdpDataStorage }
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.Exception.catching

object ClientManager {
  case class AddClient(client: ActorRef)
  case class RemoveClient(client: ActorRef)
}

class ClientManager extends Actor with LazyLogging {
  import ClientManager._

  val udpDataStorage = context.actorOf(
    Props(classOf[UdpDataStorage]), "UdpDataStorage"
  )

  override def preStart() = {
    logger.debug("ClientManager preStart.");
  }

  override def postStop() = {
    context.children.foreach { child =>
      catching(classOf[AskTimeoutException]).either {
        val stopped = gracefulStop(child, 5.seconds, PoisonPill)
        Await.result(stopped, 6.seconds)
      } match {
        case Left(e) => logger.error(e.getMessage)
        case _ => Unit
      }
    }

    logger.debug("ClientManager postStop.")
  }

  def receive() = processing(List[ActorRef]())

  private def processing(clientList: List[ActorRef]): Receive = {
    case AddClient(client) =>
      udpDataStorage ! client
      context.become(processing(clientList :+ client))
    case RemoveClient(client) =>
      context.become(processing(clientList.filter(_ != client).toList))
    case udpData: UdpData =>
      val json = udpData.toJsonString
      clientList.foreach(_ ! json)
      udpDataStorage ! (udpData, json)
    case _ =>
      logger.warn("ClientManager received unknown message.")
  }
}
