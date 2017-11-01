package com.github.nabezokodaikon.example

import akka.http.scaladsl.model.ws.TextMessage
import com.github.nabezokodaikon.pcars1._
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

  println("// TelemetryData")
  val telemetryDataName = s"${dir}/testdata/0_20171102053840991.bin"
  var telemetryDataBuffer = FileUtil.readBinary(telemetryDataName).toList
  val telemetryData = readTelemetryData(telemetryDataBuffer)
  println(telemetryData.hwState.tyreCompound(0))
  println(telemetryData.hwState.tyreCompound(1))
  println(telemetryData.hwState.tyreCompound(2))
  println(telemetryData.hwState.tyreCompound(3))

  println("// RaceData")
  val raceDataName = s"${dir}/testdata/1_20171102053447210.bin"
  var raceDataBuffer = FileUtil.readBinary(raceDataName).toList
  val raceData = readRaceData(raceDataBuffer)
  println(raceData.translatedTrackLocation)
  println(raceData.translatedTrackVariation)

  println("// ParticipantsData")
  val participantsDataName = s"${dir}/testdata/2_20171102053453356.bin"
  var participantsDataBuffer = FileUtil.readBinary(raceDataName).toList
  val participantsData = readParticipantsData(raceDataBuffer)
  println(participantsData.name.length)
  participantsData.name.foreach(println)

  println("// TimingsData")
  val timingsDataName = s"${dir}/testdata/3_20171102053850824.bin"
  var timingsDataBuffer = FileUtil.readBinary(timingsDataName).toList
  val timingsData = readTimingsData(timingsDataBuffer)
  println(timingsData.partcipants.length)

  println("// GameState")
  val gameStateDataName = s"${dir}/testdata/4_20171102053558168.bin"
  var gameStateDataBuffer = FileUtil.readBinary(gameStateDataName).toList
  val gameStateData = readGameStateData(gameStateDataBuffer)
  println(gameStateData.gameState)
  println(gameStateData.sessionState)

  println("// TimeStatsData")
  val timeStatsDataName = s"${dir}/testdata/7_20171102053641488.bin"
  var timeStatsDataBuffer = FileUtil.readBinary(timeStatsDataName).toList
  val timeStatsData = readTimeStatsData(timeStatsDataBuffer)
  println(timeStatsData.stats.participants.length)
  println(timeStatsData.stats.participants(timeStatsData.stats.participants.length - 1))

  println("// VehicleClassNamesData")
  val vehicleClassNamesDataName = s"${dir}/testdata/8_20171102053454888.bin"
  var vehicleClassNamesDataBuffer = FileUtil.readBinary(vehicleClassNamesDataName).toList
  val vehicleClassNamesData = readVehicleClassNamesData(vehicleClassNamesDataBuffer)
  println(vehicleClassNamesData.classes.length)
  println(vehicleClassNamesData.classes(0).name)

  println("// ParticipantVehicleNamesData")
  val participantVehicleNamesDataName = s"${dir}/testdata/8_20171102054033434.bin"
  var participantVehicleNamesDataBuffer = FileUtil.readBinary(participantVehicleNamesDataName).toList
  val participantVehicleNamesData = readParticipantVehicleNamesData(participantVehicleNamesDataBuffer)
  println(participantVehicleNamesData.vehicles.length)
  println(participantVehicleNamesData.vehicles(0).name)

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
