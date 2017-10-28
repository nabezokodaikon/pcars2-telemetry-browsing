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
        println("Call preStart.")
        val client = getStageActor(messageHandler).ref
        clientManager ! ClientManager.AddClient(client)
      }

      override def postStop(): Unit = {
        println("Call postStop.")
        val client = stageActor.ref
        clientManager ! ClientManager.RemoveClient(client)
      }

      private def messageHandler(receive: (ActorRef, Any)): Unit =
        receive match {
          case (_, value: UdpListener.OutgoingValue) =>
            // println("Receive value.")
            if (isAvailable(out)) {
              push(out, value)
            }
          case _ =>
            logger.warn("ClienteStage received unknown message.")
        }
    }
}
