package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging

object HttpExample extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Http"))
}
