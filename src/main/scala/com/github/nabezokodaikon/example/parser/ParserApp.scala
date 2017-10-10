package com.github.nabezokodaikon.example

import akka.http.scaladsl.model.ws.TextMessage
import com.github.nabezokodaikon.pcars1._
import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.ParticipantInfo
import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory._
import com.github.nabezokodaikon.util.Loan.runningTime
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()

  val telemetryDataName = s"${dir}/testdata/0_20171009190539027.bin"
  var telemetryDataData = FileUtil.readBinary(telemetryDataName).toList
  val telemetryData = createTelemetryData(telemetryDataData)
  println(telemetryData.carStateData.brake)
  println(telemetryData.carStateData.throttle)
  println(telemetryData.carStateData.clutch)
  println(telemetryData.carStateData.steering)
  println(telemetryData.carStateData.speed)
  println(telemetryData.carStateData.gear)
  // println(telemetryData.toJsonString)

  runningTime {
    for (i <- 0 to 0) {
      val src = createTelemetryData(telemetryDataData)
      val json = src.toJsonString
      val msg = TextMessage(json)
    }
  }

  // println("////////////////////////////////")

  val participantInfoStringsName = s"${dir}/testdata/1_20171008215718126.bin"
  var participantInfoStringsData = FileUtil.readBinary(participantInfoStringsName).toList
  val participantInfoStrings = createParticipantInfoStrings(participantInfoStringsData)
  println(participantInfoStrings.carName)
  println(participantInfoStrings.carClassName)
  println(participantInfoStrings.trackLocation)
  println(participantInfoStrings.trackVariation)
  println(participantInfoStrings.nameString(0))
  // println(participantInfoStrings.toJsonString)

  runningTime {
    for (i <- 0 to 0) {
      val src = createParticipantInfoStrings(participantInfoStringsData)
      val json = src.toJsonString
      val msg = TextMessage(json)
    }
  }
}
