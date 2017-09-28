package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging

object ActorExample extends App with LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  logger.info(helloWorld("Actor"))
}
