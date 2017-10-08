package com.github.nabezokodaikon.example

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.ParticipantInfo
import com.github.nabezokodaikon.pcars1.SharedMemoryConstants._
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171007151454008.bin"
  var pcarsData = FileUtil.readBinary(name).toList
  // println(pcarsData.length)
  // println(pcarsData.toString)

  val frameTypeAndSequence: Int = pcarsData(FRAME_TYPEAND_SEQUENCE)
  val frameType = frameTypeAndSequence & 3
  println(frameType)
  val sequence = frameTypeAndSequence >> 2

  /*
   *  sTelemetryData
   */
}
