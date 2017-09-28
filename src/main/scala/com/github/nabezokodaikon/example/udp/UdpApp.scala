package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging

object UdpApp extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("UDP"))
}
