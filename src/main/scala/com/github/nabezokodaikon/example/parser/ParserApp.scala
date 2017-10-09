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

  val telemetryDataName = s"${dir}/testdata/0_20171007151455850.bin"
  var telemetryDataData = FileUtil.readBinary(telemetryDataName).toList
  val telemetryData = createTelemetryData(telemetryDataData)
  println(telemetryData.brake)
  println(telemetryData.throttle)
  println(telemetryData.clutch)
  println(telemetryData.steering)
  println(telemetryData.speed)
  println(telemetryData.gearNumGears)

  {
    val start = System.currentTimeMillis
    for (i <- 0 to 60) {
      createTelemetryData(telemetryDataData)
    }
    val interval = System.currentTimeMillis - start
    println(s"${interval} msec")
  }

  println("////////////////////////////////")

  val participantInfoStringsName = s"${dir}/testdata/1_20171008215718126.bin"
  var participantInfoStringsData = FileUtil.readBinary(participantInfoStringsName).toList
  val participantInfoStrings = createParticipantInfoStrings(participantInfoStringsData)
  println(participantInfoStrings.carName)
  println(participantInfoStrings.carClassName)
  println(participantInfoStrings.trackLocation)
  println(participantInfoStrings.trackVariation)
  println(participantInfoStrings.nameString(0))

  {
    val start = System.currentTimeMillis
    for (i <- 0 to 60) {
      createParticipantInfoStrings(participantInfoStringsData)
    }
    val interval = System.currentTimeMillis - start
    println(s"${interval} msec")
  }
}
