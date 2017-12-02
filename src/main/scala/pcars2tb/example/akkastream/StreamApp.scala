/*
package com.github.nabezokodaikon.example.akkastream

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent._
import akka._
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.util._

case class Connected(value: Int)
case class Send(msg: String)

// class Listener extends Actor {

// def receive = {
// case
// }
// }

// class Client extends Actor {

// def receive = {
// }
// }

object StreamApp extends App with LazyLogging {

  implicit val system = ActorSystem("TestSystem")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Akka Stream"))

  // def run(actor: ActorRef) = {
  // Future { Thread.sleep(300); actor ! 1 }
  // Future { Thread.sleep(200); actor ! 2 }
  // Future { Thread.sleep(100); actor ! 3 }
  // }
  // val s = Source
  // .actorRef[Int](bufferSize = 0, OverflowStrategy.fail)
  // .mapMaterializedValue(run)

  // s runForeach println
  // val s = Source.empty
  // s.run
}
*/
