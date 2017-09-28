package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.io.{ IO, Udp }
import akka.pattern.{ gracefulStop, AskTimeoutException }
import com.typesafe.scalalogging.LazyLogging
import java.net.InetSocketAddress
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.io.StdIn.readLine
import scala.language.postfixOps

class Listener extends Actor {
  import context.system
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("0.0.0.0", 5606))

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
    case _ =>
      println("received unknown message")
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      println(s"$remote")
      println(s"$data.toString")
    case Udp.Unbind =>
      println("unbind")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      println("unbound")
      context.stop(self)
  }
}

object UdpApp extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("UDP"))

  readLine("Press the Enter key to exit.\n")

  val system = ActorSystem("mySystem")
  val props = Props(classOf[Listener])
  val myActor = system.actorOf(props, "myActor")

  try {
    val stopped: Future[Boolean] = gracefulStop(myActor, 5 seconds, system.terminate)
    Await.result(stopped, 6 seconds)
    println("success")
  } catch {
    case e: akka.pattern.AskTimeoutException =>
      println(s"timeout: $e.message")
  }
}
