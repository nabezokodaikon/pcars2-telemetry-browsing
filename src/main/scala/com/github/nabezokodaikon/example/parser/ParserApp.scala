package com.github.nabezokodaikon.example

import com.github.nabezokodaikon.pcars1._
import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.ParticipantInfo
import com.github.nabezokodaikon.pcars1.SharedMemoryConstants._
import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory._
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/1_20171008215718126.bin"
  var data = FileUtil.readBinary(name).toList
  println(data.length)
  // println(data.toString)
}
