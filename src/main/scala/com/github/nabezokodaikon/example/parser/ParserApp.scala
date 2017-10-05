package com.github.nabezokodaikon.example

import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171005120800046.bin"
  val data = FileUtil.readBinary(name).toSeq
  println(data.toString)
}
