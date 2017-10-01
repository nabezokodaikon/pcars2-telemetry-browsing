package com.github.nabezokodaikon

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.pattern.{ gracefulStop, AskTimeoutException }
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

object ActorDone

object Main extends App with LazyLogging {
  implicit val system = ActorSystem("pcars2-udp-app")
  implicit val materializer = ActorMaterializer()

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("nabezokodaikokn"))

  val udpProps = Props(classOf[UdpListener])
  val udpListener = system.actorOf(udpProps, "udpListener")

  val httpRoute = HttpServer.route
  Http().bindAndHandle(httpRoute, "192.168.1.18", 9000)
  println("Started server at 192.168.1.18:9000, press enter to stop server")
  StdIn.readLine()

  try {
    val stopped = gracefulStop(udpListener, 5 seconds, ActorDone)
    Await.result(stopped, 6 seconds)
  } catch {
    case e: AskTimeoutException =>
      logger.error(e.getMessage)
  }

  system.terminate()
}
