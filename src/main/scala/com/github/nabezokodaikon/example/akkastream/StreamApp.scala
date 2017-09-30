package com.github.nabezokodaikon.example.akkastream

import com.typesafe.scalalogging.LazyLogging

object StreamApp extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Akka Stream"))
}
