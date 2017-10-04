package com.github.nabezokodaikon

import akka.actor.{ ActorRef, Props }
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.stream.{ Attributes, FlowShape, Inlet, Outlet }
import akka.stream.SourceShape
import akka.stream.stage.{ GraphStage, GraphStageLogic, InHandler, OutHandler }
import com.typesafe.scalalogging.LazyLogging

class ClientStage(clientManager: ActorRef)
    extends GraphStage[SourceShape[UdpListener.OutgoingValue]]
  with LazyLogging {

  // val in: Inlet[Message] = Inlet("ClientStage-In")
  val out: Outlet[UdpListener.OutgoingValue] = Outlet("ClientStage-Out")

  override val shape = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      import UsingActor._

      val props = Props(classOf[Client], clientManager)
      val client = system.actorOf(props)

      // setHandler(in, new InHandler {
        // override def onPush(): Unit = {
          // println("Call onPush.")
          // val msg = grab(in) match {
            // case TextMessage.Strict(text) => text
            // case _ => "Received unknown message."
          // }
          // println(msg)
        // }
      // })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          println("Call onPull.")

          val outgoing = getStageActor(messageHandler).ref
          println(stageActor.ref.toString)
          client ! Client.Connected(outgoing)
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
          case (a, receivedData) =>
            receivedData match {
              case nextData: UdpListener.OutgoingValue =>
                println("Receive data.")
                println(a.toString)
                println(nextData.toString)
                push(out, nextData)
              case _ => 
                logger.warn("Received unknown message.")
            }
          case _ =>
            logger.warn("Received unknown message.")
        }
    }
}
