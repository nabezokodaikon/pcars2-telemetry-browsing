package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{ HttpApp, Route }

object WebServer extends HttpApp {

  override def routes: Route = {
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Hello Akka Http</h1>"))
      }
    }
  }
}

object HttpExample extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Http"))

  val system = ActorSystem("webserver")
  WebServer.startServer("192.168.1.18", 9000, system)
  system.terminate
}
