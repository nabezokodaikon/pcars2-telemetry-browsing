package com.github.nabezokodaikon

import akka.actor.{ ActorRef }
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

      setHandler(out, new OutHandler {
        override def onPull(): Unit = ()
      })

      override def preStart(): Unit = {
        val client = getStageActor(messageHandler).ref
        clientManager ! ClientManager.AddClient(client)
      }

      override def postStop(): Unit = {
        val client = stageActor.ref
        clientManager ! ClientManager.RemoveClient(client)
      }

      private def messageHandler(receive: (ActorRef, Any)): Unit =
        receive match {
          case (_, value: UdpListener.OutgoingValue) if isAvailable(out) =>
            push(out, value)
          case (_, value: UdpListener.OutgoingValue) =>
            Unit
          case _ =>
            logger.warn("ClienteStage received unknown message.")
        }
    }
}
