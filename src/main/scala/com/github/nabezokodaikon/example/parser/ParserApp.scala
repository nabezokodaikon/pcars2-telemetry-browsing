package com.github.nabezokodaikon.example

import ch.ethz.acl.passera.unsigned._
import com.github.nabezokodaikon.util.BinaryUtil
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171005120800046.bin"
  var data = FileUtil.readBinary(name).toList
  println(data.toString)

  val (value, unValueData) = BinaryUtil.readUInt(data)
  println(value.toString)
}
