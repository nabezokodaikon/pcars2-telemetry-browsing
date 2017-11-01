package com.github.nabezokodaikon.example

import akka.http.scaladsl.model.ws.TextMessage
import com.github.nabezokodaikon.pcars1._
import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.ParticipantInfo
import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory._
import com.github.nabezokodaikon.pcars2._
import com.github.nabezokodaikon.pcars2.UDPDataReader._
import com.github.nabezokodaikon.util.Loan.runningTime
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.currentDirectory
  println("parser")

  val gameStateName = s"${dir}/testdata/TimingsData_20171101102119402.bin"
  var gameStateData = FileUtil.readBinary(gameStateName).toList
  val gameState = readPacketBase(gameStateData)
  println(gameState.packetNumber)
  println(gameState.categoryPacketNumber)
  println(gameState.partialPacketIndex)
  println(gameState.partialPacketNumber)
  println(gameState.packetType)
  println(gameState.packetVersion)

  // val gameStateName = s"${dir}/testdata/4_GameState_20171101102112656.bin"
  // var gameStateData = FileUtil.readBinary(gameStateName).toList
  // val gameState = readPacketBase(gameStateData)
  // println(gameState.packetNumber)
  // println(gameState.categoryPacketNumber)
  // println(gameState.partialPacketIndex)
  // println(gameState.partialPacketNumber)
  // println(gameState.packetType)
  // println(gameState.packetVersion)

  // println(telemetryData.carStateData.brake)
  // println(telemetryData.carStateData.throttle)
  // println(telemetryData.carStateData.clutch)
  // println(telemetryData.carStateData.steering)
  // println(telemetryData.carStateData.speed)
  // println(telemetryData.carStateData.gear)
  // println(telemetryData.tyreUdpData.airPressure(0))
  // println(telemetryData.tyreUdpData.airPressure(1))
  // println(telemetryData.tyreUdpData.airPressure(2))
  // println(telemetryData.tyreUdpData.airPressure(3))
  // println(telemetryData.toJsonString)

  // runningTime {
  // for (i <- 0 to 1000) {
  // val src = createTelemetryData(telemetryDataData)
  // val json = src.toJsonString
  // val msg = TextMessage(json)
  // }
  // }

  // println("////////////////////////////////")

  // val participantInfoStringsName = s"${dir}/testdata/1_20171011162536537.bin"
  // var participantInfoStringsData = FileUtil.readBinary(participantInfoStringsName).toList
  // val participantInfoStrings = createParticipantInfoStrings(participantInfoStringsData)
  // // println(participantInfoStrings.carName)
  // // println(participantInfoStrings.carClassName)
  // // println(participantInfoStrings.trackLocation)
  // // println(participantInfoStrings.trackVariation)
  // // println(participantInfoStrings.nameString(0))
  // // println(participantInfoStrings.toJsonString)

  // runningTime {
  // for (i <- 0 to 1000) {
  // val src = createParticipantInfoStrings(participantInfoStringsData)
  // val json = src.toJsonString
  // val msg = TextMessage(json)
  // }
  // }
}
