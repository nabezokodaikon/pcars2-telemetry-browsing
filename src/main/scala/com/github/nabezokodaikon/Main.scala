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

  val udpProps = Props(classOf[UdpListener])
  val udpListener = system.actorOf(udpProps, "udpListener")

  // implicit val timeout = Timeout(5.seconds)
  // val a = (udpListener ? "Taro")(5.seconds).mapTo[String]
  // a.onComplete {
  // case Success(m) => println(m)
  // case Failure(_) => println
  // }

  // val b = (udpListener ? "Jiro")(5.seconds).mapTo[String]
  // b.onComplete {
  // case Success(m) => println(m)
  // case Failure(_) => println
  // }

  val httpRoute = HttpServer.route
  Http().bindAndHandle(httpRoute, "192.168.1.18", 9000)
  println("Started server at 192.168.1.18:9000, press enter to stop server")
  StdIn.readLine()

  try {
    val stopped = gracefulStop(udpListener, 5.seconds, ActorDone)
    Await.result(stopped, 6.seconds)
  } catch {
    case e: AskTimeoutException =>
      logger.error(e.getMessage)
  }

  system.terminate()
}
