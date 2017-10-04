package com.github.nabezokodaikon

import akka.actor.{ ActorRef, Props }
import akka.stream.{ Attributes, Outlet }
import akka.stream.SourceShape
import akka.stream.stage.{ GraphStage, GraphStageLogic, OutHandler }
import com.typesafe.scalalogging.LazyLogging

class ClientStage(clientManager: ActorRef)
  extends GraphStage[SourceShape[UdpListener.OutgoingValue]]
  with LazyLogging {

  val out: Outlet[UdpListener.OutgoingValue] = Outlet("ClientStage-Out")

  override val shape = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      import UsingActor._

      val props = Props(classOf[Client], clientManager)
      val client = system.actorOf(props)

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          println("Call onPull.")
        }
      })

      override def preStart(): Unit = {
        println("Call preStart.")

        val outgoing = getStageActor(messageHandler).ref
        println(stageActor.ref.toString)
        client ! Client.Connected(stageActor.ref)
        clientManager ! ClientManager.AddClient(client)
      }

      override def postStop(): Unit = {
        println("Call postStop.")
        clientManager ! ClientManager.RemoveClient(client)
      }

      private def messageHandler(receive: (ActorRef, Any)): Unit =
        receive match {
          case (_, value: UdpListener.OutgoingValue) =>
            println("Receive value.")
            push(out, value)
          case _ =>
            logger.warn("Received unknown message.")
        }
    }
}
