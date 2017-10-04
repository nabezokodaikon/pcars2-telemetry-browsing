package com.github.nabezokodaikon

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.pattern.{ AskTimeoutException, ask, gracefulStop }
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import scala.util.{ Failure, Success }

object ActorDone

object UsingActor {
  implicit val system = ActorSystem("pcars2-udp-app")
  implicit val materializer = ActorMaterializer()
}

object Main extends App with LazyLogging {
  import UsingActor._

  val clientManagerProps = Props(classOf[ClientManager])
  val clientManager = system.actorOf(clientManagerProps, "clientManager")

  val udpProps = Props(classOf[UdpListener], clientManager)
  val udpListener = system.actorOf(udpProps, "udpListener")

  val server = new Server(clientManager)
  val httpRoute = server.route
  Http().bindAndHandle(httpRoute, "192.168.1.18", 9000)
  println("Started server at 192.168.1.18:9000, press enter to stop server")

  while (true) {
    Thread.sleep(1000)
    udpListener ! java.util.Calendar.getInstance().getTime().toString
  }

  StdIn.readLine()

  try {
    val stopped = gracefulStop(udpListener, 5.seconds, ActorDone)
    Await.result(stopped, 6.seconds)
  } catch {
    case e: AskTimeoutException =>
      logger.error(e.getMessage)
  }

  try {
    val stopped = gracefulStop(clientManager, 5.seconds, ActorDone)
    Await.result(stopped, 6.seconds)
  } catch {
    case e: AskTimeoutException =>
      logger.error(e.getMessage)
  }

  system.terminate()
}
