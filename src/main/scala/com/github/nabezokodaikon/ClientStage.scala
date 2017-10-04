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
        client ! Client.Connected(outgoing)
        clientManager ! ClientManager.AddClient(client)
      }

      override def postStop(): Unit = {
        println("Outgoing stop.")
      }

      private def messageHandler(receive: (ActorRef, Any)): Unit =
        receive match {
          case (a, data: UdpListener.OutgoingValue) =>
            println("Receive data.")
            println(a.toString)
            println(data.toString)
            push(out, data)
          case _ =>
            logger.warn("Received unknown message.")
        }
    }
}
