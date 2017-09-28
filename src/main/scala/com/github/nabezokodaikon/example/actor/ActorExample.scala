package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging
import akka.actor.{ Actor, ActorSystem, Props }
import akka.pattern.gracefulStop
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.language.postfixOps

class MyActor extends Actor {

  def receive = {
    case "test" => println("received test")
    case _ => println("received unknown message")
  }

  override def postStop = {
    println("stop")
  }
}

object ActorExample extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Actor"))

  val system = ActorSystem("mySystem")
  val props = Props(classOf[MyActor])
  val myActor = system.actorOf(props, "myActor")

  myActor ! "test"

  try {
    val stopped: Future[Boolean] = gracefulStop(myActor, 5 seconds, system.terminate)
    Await.result(stopped, 6 seconds)
    println("success")
  } catch {
    case e: akka.pattern.AskTimeoutException =>
      println(s"timeout: $e.message")
  }
}
