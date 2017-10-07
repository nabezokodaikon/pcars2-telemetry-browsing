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
  val (buildVersionNumber, packetTypeData) = readShort(pcarsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(buildVersionNumber)

  val (packetType, gameSessionStateData) = readUByte(packetTypeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(packetType)

  // Game states
  println("// Game states")
  val (gameSessionState, viewedParticipantIndexData) = readUByte(gameSessionStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(gameSessionState)

  // Participant info
  println("// Participant info")
  val (viewedParticipantIndex, numParticipantsData) = readByte(viewedParticipantIndexData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(viewedParticipantIndex)

  val (numParticipants, unfilteredThrottleData) = readByte(numParticipantsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(numParticipants)

  // Unfiltered input
  println("// Unfiltered input")
  val (unfilteredThrottle, unfilteredBrakeData) = readUByte(unfilteredThrottleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(unfilteredThrottle)

  val (unfilteredBrake, unfilteredSteeringData) = readUByte(unfilteredBrakeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(unfilteredBrake)

  val (unfilteredSteering, unfilteredClutchData) = readByte(unfilteredSteeringData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(unfilteredSteering)

  val (unfilteredClutch, raceStateFlagsData) = readUByte(unfilteredClutchData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(unfilteredClutch)

  val (raceStateFlags, lapsInEventData) = readUByte(raceStateFlagsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(raceStateFlags)

  // Event information
  println("// Event information")
  val (lapsInEvent, bestLapTimeData) = readUByte(lapsInEventData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(lapsInEvent)

  // Timings
  println("// Timings")
  val (bestLapTime, lastLapTimeData) = readFloat(bestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(bestLapTime)

  val (lastLapTime, currentTimeData) = readFloat(lastLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(lastLapTime)

  val (currentTime, splitTimeAheadData) = readFloat(currentTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(currentTime)

  val (splitTimeAhead, splitTimeBehindData) = readFloat(splitTimeAheadData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(splitTimeAhead)

  val (splitTimeBehind, splitTimeData) = readFloat(splitTimeBehindData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(splitTimeBehind)

  val (splitTime, eventTimeRemainingData) = readFloat(splitTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(splitTime)

  val (eventTimeRemaining, personalFastestLapTimeData) = readFloat(eventTimeRemainingData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(eventTimeRemaining)

  val (personalFastestLapTime, worldFastestLapTimeData) = readFloat(personalFastestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(personalFastestLapTime)

  val (worldFastestLapTime, currentSector1TimeData) = readFloat(worldFastestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(worldFastestLapTime)

  val (currentSector1, currentSector2TimeData) = readFloat(currentSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(currentSector1)

  val (currentSector2, currentSector3TimeData) = readFloat(currentSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(currentSector2)

  val (currentSector3, fastestSector1TimeData) = readFloat(currentSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(currentSector3)

  val (fastestSector1, fastestSector2TimeData) = readFloat(fastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(fastestSector1)

  val (fastestSector2, fastestSector3TimeData) = readFloat(fastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(fastestSector2)

  val (fastestSector3, personalFastestSector1TimeData) = readFloat(fastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(fastestSector3)

  val (personalFastestSector1Time, personalFastestSector2TimeData) = readFloat(personalFastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(personalFastestSector1Time)

  val (personalFastestSector2Time, personalFastestSector3TimeData) = readFloat(personalFastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(personalFastestSector2Time)

  val (personalFastestSector3Time, worldFastestSector1TimeData) = readFloat(personalFastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(personalFastestSector3Time)

  val (worldFastestSector1, worldFastestSector2TimeData) = readFloat(worldFastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(worldFastestSector1)

  val (worldFastestSector2, worldFastestSector3TimeData) = readFloat(worldFastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(worldFastestSector2)

  val (worldFastestSector3, joyPad1Data) = readFloat(worldFastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(worldFastestSector3)

  val (joyPad1, joyPad2Data) = readUByte(joyPad1Data) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(joyPad1)

  val (joyPad2, highestFlagData) = readUByte(joyPad2Data) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(joyPad2)

  // Flags
  println("// Flags")
  val (highestFlag, pitModeScheduleData) = readUByte(highestFlagData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(highestFlag)

  // Pit info
  println("// Pit info")
  val (pitModeSchedule, oilTempCelsiusData) = readUByte(pitModeScheduleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(pitModeSchedule)

  // Car state
  println("// Car state")
  val (oilTempCelsius, oilPressureKPaData) = readShort(oilTempCelsiusData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(oilTempCelsius)

  val (oilPressureKPa, waterTempCelsiusData) = readShort(oilPressureKPaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(oilPressureKPa)

  val (waterTempCelsius, waterPressureKpaData) = readShort(waterTempCelsiusData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(waterTempCelsius)

  val (waterPressureKpa, fuelPressureKpaData) = readShort(waterPressureKpaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(waterPressureKpa)

  val (fuelPressureKpa, carFlagsData) = readShort(fuelPressureKpaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(fuelPressureKpa)

  val (carFlags, fuelCapacityData) = readUByte(carFlagsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(carFlags)

  val (fuelCapacity, brakeData) = readUByte(fuelCapacityData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(fuelCapacity)

  val (brake, throttleData) = readUByte(brakeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(brake)

  val (throttle, clutchData) = readUByte(throttleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(throttle)

  val (clutch, steeringData) = readUByte(clutchData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(clutch)

  val (steering, fuelLevelData) = readByte(steeringData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(steering)

  val (fuelLevel, speedData) = readFloat(fuelLevelData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(fuelLevel)

  val (speed, rpmData) = readFloat(speedData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(speed)

  val (rpm, maxRpmData) = readShort(rpmData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(rpm)

  val (maxRpm, gearNumGearsData) = readShort(maxRpmData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(maxRpm)

  val (gearNumGears, boostAmountData) = readUByte(gearNumGearsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(gearNumGears)

  val (boostAmount, enforcedPitStopLapData) = readUByte(boostAmountData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(boostAmount)

  val (enforcedPitStopLap, crashStateData) = readByte(enforcedPitStopLapData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(enforcedPitStopLap)

  val (crashState, odometerKMData) = readUByte(crashStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(crashState)

  val (odometerKM, orientationData) = readFloat(odometerKMData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(odometerKM)

  val orientationArray = Array.fill(3)(0f)
  var orientationCurrenttData = orientationData
  for (i <- 0 to orientationArray.length - 1) {
    val (value, nextData) = readFloat(orientationCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    orientationArray(i) = value
    orientationCurrenttData = nextData
    println(orientationArray(i))
  }

  val localVelocityArray = Array.fill(3)(0f)
  var localVelocityCurrenttData = orientationCurrenttData
  for (i <- 0 to localVelocityArray.length - 1) {
    val (value, nextData) = readFloat(localVelocityCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    localVelocityArray(i) = value
    localVelocityCurrenttData = nextData
    println(localVelocityArray(i))
  }

  val worldVelocityArray = Array.fill(3)(0f)
  var worldVelocityCurrenttData = localVelocityCurrenttData
  for (i <- 0 to worldVelocityArray.length - 1) {
    val (value, nextData) = readFloat(worldVelocityCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    worldVelocityArray(i) = value
    worldVelocityCurrenttData = nextData
    println(worldVelocityArray(i))
  }

  val angularVelocityArray = Array.fill(3)(0f)
  var angularVelocityCurrenttData = worldVelocityCurrenttData
  for (i <- 0 to angularVelocityArray.length - 1) {
    val (value, nextData) = readFloat(angularVelocityCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    angularVelocityArray(i) = value
    angularVelocityCurrenttData = nextData
    println(angularVelocityArray(i))
  }

  val localAccelerationArray = Array.fill(3)(0f)
  var localAccelerationCurrenttData = angularVelocityCurrenttData
  for (i <- 0 to localAccelerationArray.length - 1) {
    val (value, nextData) = readFloat(localAccelerationCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    localAccelerationArray(i) = value
    localAccelerationCurrenttData = nextData
    println(localAccelerationArray(i))
  }

  val worldAccelerationArray = Array.fill(3)(0f)
  var worldAccelerationCurrenttData = localAccelerationCurrenttData
  for (i <- 0 to worldAccelerationArray.length - 1) {
    val (value, nextData) = readFloat(worldAccelerationCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    worldAccelerationArray(i) = value
    worldAccelerationCurrenttData = nextData
    println(worldAccelerationArray(i))
  }

  val extentsCentreArray = Array.fill(3)(0f)
  var extentsCentreCurrenttData = worldAccelerationCurrenttData
  for (i <- 0 to extentsCentreArray.length - 1) {
    val (value, nextData) = readFloat(extentsCentreCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    extentsCentreArray(i) = value
    extentsCentreCurrenttData = nextData
    println(extentsCentreArray(i))
  }

  // Wheels / Tyres
  println("// Wheels / Tyres")
  val tyreFlagsArray = Array.fill(4)(0)
  var tyreFlagsCurrenttData = extentsCentreCurrenttData
  for (i <- 0 to tyreFlagsArray.length - 1) {
    val (value, nextData) = readUByte(tyreFlagsCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    tyreFlagsArray(i) = value
    tyreFlagsCurrenttData = nextData
    println(tyreFlagsArray(i))
  }

  val terrainArray = Array.fill(4)(0)
  var terrainCurrenttData = tyreFlagsCurrenttData
  for (i <- 0 to terrainArray.length - 1) {
    val (value, nextData) = readUByte(terrainCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    terrainArray(i) = value
    terrainCurrenttData = nextData
    println(terrainArray(i))
  }

  val tyreYArray = Array.fill(4)(0f)
  var tyreYCurrenttData = terrainCurrenttData
  for (i <- 0 to tyreYArray.length - 1) {
    val (value, nextData) = readFloat(tyreYCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    tyreYArray(i) = value
    tyreYCurrenttData = nextData
    println(tyreYArray(i))
  }

  val tyreRPSArray = Array.fill(4)(0f)
  var tyreRPSCurrenttData = tyreYCurrenttData
  for (i <- 0 to tyreRPSArray.length - 1) {
    val (value, nextData) = readFloat(tyreRPSCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    tyreRPSArray(i) = value
    tyreRPSCurrenttData = nextData
    println(tyreRPSArray(i))
  }

  val tyreSlipSpeedArray = Array.fill(4)(0f)
  var tyreSlipSpeedCurrenttData = tyreRPSCurrenttData
  for (i <- 0 to tyreSlipSpeedArray.length - 1) {
    val (value, nextData) = readFloat(tyreSlipSpeedCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    tyreSlipSpeedArray(i) = value
    tyreSlipSpeedCurrenttData = nextData
    println(tyreSlipSpeedArray(i))
  }

  val tyreTempArray = Array.fill(4)(0)
  var tyreTempCurrenttData = tyreSlipSpeedCurrenttData
  for (i <- 0 to tyreTempArray.length - 1) {
    val (value, nextData) = readUByte(tyreTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    tyreTempArray(i) = value
    tyreTempCurrenttData = nextData
    println(tyreTempArray(i))
  }

  val tyreGripArray = Array.fill(4)(0)
  var tyreGripCurrenttData = tyreTempCurrenttData
  for (i <- 0 to tyreGripArray.length - 1) {
    val (value, nextData) = readUByte(tyreGripCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    tyreGripArray(i) = value
    tyreGripCurrenttData = nextData
    println(tyreGripArray(i))
  }

  val tyreHeightAboveGroundArray = Array.fill(4)(0f)
  var tyreHeightAboveGroundCurrenttData = tyreGripCurrenttData
  for (i <- 0 to tyreHeightAboveGroundArray.length - 1) {
    val (value, nextData) = readFloat(tyreHeightAboveGroundCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    tyreHeightAboveGroundArray(i) = value
    tyreHeightAboveGroundCurrenttData = nextData
    println(tyreHeightAboveGroundArray(i))
  }

  val tyreLateralStiffnessArray = Array.fill(4)(0f)
  var tyreLateralStiffnessCurrenttData = tyreHeightAboveGroundCurrenttData
  for (i <- 0 to tyreLateralStiffnessArray.length - 1) {
    val (value, nextData) = readFloat(tyreLateralStiffnessCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    tyreLateralStiffnessArray(i) = value
    tyreLateralStiffnessCurrenttData = nextData
    println(tyreLateralStiffnessArray(i))
  }

  val tyreWearArray = Array.fill(4)(0)
  var tyreWearCurrenttData = tyreLateralStiffnessCurrenttData
  for (i <- 0 to tyreWearArray.length - 1) {
    val (value, nextData) = readUByte(tyreWearCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    tyreWearArray(i) = value
    tyreWearCurrenttData = nextData
    println(tyreWearArray(i))
  }

  val brakeDamageArray = Array.fill(4)(0)
  var brakeDamageCurrenttData = tyreWearCurrenttData
  for (i <- 0 to brakeDamageArray.length - 1) {
    val (value, nextData) = readUByte(brakeDamageCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    brakeDamageArray(i) = value
    brakeDamageCurrenttData = nextData
    println(brakeDamageArray(i))
  }

  val suspensionDamageArray = Array.fill(4)(0)
  var suspensionDamageCurrenttData = brakeDamageCurrenttData
  for (i <- 0 to suspensionDamageArray.length - 1) {
    val (value, nextData) = readUByte(suspensionDamageCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    suspensionDamageArray(i) = value
    suspensionDamageCurrenttData = nextData
    println(suspensionDamageArray(i))
  }

  val brakeTempCelsiusArray = Array.fill(4)(0)
  var brakeTempCelsiusCurrenttData = suspensionDamageCurrenttData
  for (i <- 0 to brakeTempCelsiusArray.length - 1) {
    val (value, nextData) = readShort(brakeTempCelsiusCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    brakeTempCelsiusArray(i) = value
    brakeTempCelsiusCurrenttData = nextData
    println(brakeTempCelsiusArray(i))
  }

  val tyreTreadTempArray = Array.fill(4)(0)
  var tyreTreadTempCurrenttData = brakeTempCelsiusCurrenttData
  for (i <- 0 to tyreTreadTempArray.length - 1) {
    val (value, nextData) = readShort(tyreTreadTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    tyreTreadTempArray(i) = value
    tyreTreadTempCurrenttData = nextData
    println(tyreTreadTempArray(i))
  }

  val tyreLayerTempArray = Array.fill(4)(0)
  var tyreLayerTempCurrenttData = tyreTreadTempCurrenttData
  for (i <- 0 to tyreLayerTempArray.length - 1) {
    val (value, nextData) = readShort(tyreLayerTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    tyreLayerTempArray(i) = value
    tyreLayerTempCurrenttData = nextData
    println(tyreLayerTempArray(i))
  }

  val tyreCarcassTempArray = Array.fill(4)(0)
  var tyreCarcassTempCurrenttData = tyreLayerTempCurrenttData
  for (i <- 0 to tyreCarcassTempArray.length - 1) {
    val (value, nextData) = readShort(tyreCarcassTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    tyreCarcassTempArray(i) = value
    tyreCarcassTempCurrenttData = nextData
    println(tyreCarcassTempArray(i))
  }

  val tyreRimTempArray = Array.fill(4)(0)
  var tyreRimTempCurrenttData = tyreCarcassTempCurrenttData
  for (i <- 0 to tyreRimTempArray.length - 1) {
    val (value, nextData) = readShort(tyreRimTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    tyreRimTempArray(i) = value
    tyreRimTempCurrenttData = nextData
    println(tyreRimTempArray(i))
  }

  val tyreInternalAirTempArray = Array.fill(4)(0)
  var tyreInternalAirTempCurrenttData = tyreRimTempCurrenttData
  for (i <- 0 to tyreInternalAirTempArray.length - 1) {
    val (value, nextData) = readShort(tyreInternalAirTempCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    tyreInternalAirTempArray(i) = value
    tyreInternalAirTempCurrenttData = nextData
    println(tyreInternalAirTempArray(i))
  }

  val wheelLocalPositionYArray = Array.fill(4)(0f)
  var wheelLocalPositionYCurrenttData = tyreInternalAirTempCurrenttData
  for (i <- 0 to wheelLocalPositionYArray.length - 1) {
    val (value, nextData) = readFloat(wheelLocalPositionYCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    wheelLocalPositionYArray(i) = value
    wheelLocalPositionYCurrenttData = nextData
    println(wheelLocalPositionYArray(i))
  }

  val rideHeightArray = Array.fill(4)(0f)
  var rideHeightCurrenttData = wheelLocalPositionYCurrenttData
  for (i <- 0 to rideHeightArray.length - 1) {
    val (value, nextData) = readFloat(rideHeightCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    rideHeightArray(i) = value
    rideHeightCurrenttData = nextData
    println(rideHeightArray(i))
  }

  val suspensionTravelArray = Array.fill(4)(0f)
  var suspensionTravelCurrenttData = rideHeightCurrenttData
  for (i <- 0 to suspensionTravelArray.length - 1) {
    val (value, nextData) = readFloat(suspensionTravelCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    suspensionTravelArray(i) = value
    suspensionTravelCurrenttData = nextData
    println(suspensionTravelArray(i))
  }

  val suspensionVelocityArray = Array.fill(4)(0f)
  var suspensionVelocityCurrenttData = suspensionTravelCurrenttData
  for (i <- 0 to suspensionVelocityArray.length - 1) {
    val (value, nextData) = readFloat(suspensionVelocityCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }
    suspensionVelocityArray(i) = value
    suspensionVelocityCurrenttData = nextData
    println(suspensionVelocityArray(i))
  }

  val airPressureArray = Array.fill(4)(0)
  var airPressureCurrenttData = suspensionVelocityCurrenttData
  for (i <- 0 to airPressureArray.length - 1) {
    val (value, nextData) = readShort(airPressureCurrenttData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }
    airPressureArray(i) = value
    airPressureCurrenttData = nextData
    println(airPressureArray(i))
  }

  // Extras
  println("// Extras")
  val (engineSpeed, engineTorqueData) = readFloat(airPressureCurrenttData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(engineSpeed)

  val (engineTorque, aeroDamageData) = readFloat(engineTorqueData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(engineTorque)

  // Car damage
  println("// Car damage")
  val (aeroDamage, engineDamageData) = readUByte(aeroDamageData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(aeroDamage)

  val (engineDamage, ambientTemperatureData) = readUByte(engineDamageData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(engineDamage)

  // Weather
  println("// Weather")
  val (ambientTemperature, trackTemperatureData) = readByte(ambientTemperatureData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(ambientTemperature)

  val (trackTemperature, rainDensityData) = readByte(trackTemperatureData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(trackTemperature)

  val (rainDensity, windSpeedData) = readUByte(rainDensityData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(rainDensity)

  val (windSpeed, windDirectionXData) = readByte(windSpeedData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(windSpeed)

  val (windDirectionX, windDirectionYData) = readByte(windDirectionXData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(windDirectionX)

  val (windDirectionY, participantInfoData) = readByte(windDirectionYData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(windDirectionY)

  val participantInfoArray = Array.fill(56)(PARTICIPANT_INFO_EMPTY)
  var participantInfoCurrentData = participantInfoData
  for (i <- 0 to 55) {

    val worldPositionArray = Array.fill(3)(0: Short)
    var worldPositionCurrentData = participantInfoCurrentData
    for (j <- 0 to 2) {
      val (value, nextData) = readShort(worldPositionCurrentData) match {
        case Some((v, d)) => (v, d)
        case None => (0: Short, Nil)
      }
      worldPositionArray(j) = value
      worldPositionCurrentData = nextData
    }

    val (currentLapDistance, racePositionData) = readShort(worldPositionCurrentData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }

    val (racePosition, lapsCompletedData) = readUByte(racePositionData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (lapsCompleted, currentLapData) = readUByte(lapsCompletedData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (currentLap, sectorData) = readUByte(currentLapData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (sector, lastSectorTimeData) = readUByte(sectorData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (lastSectorTime, nextData) = readFloat(lastSectorTimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    participantInfoCurrentData = nextData

    participantInfoArray(i) = ParticipantInfo(
      worldPosition = worldPositionArray,
      currentLapDistance = currentLapDistance,
      racePosition = racePosition,
      lapsCompleted = lapsCompleted,
      currentLap = currentLap,
      sector = sector,
      lastSectorTime = lastSectorTime)

    println(participantInfoArray(i))
  }

  val (trackLength, wingsData) = readFloat(participantInfoCurrentData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }
  println(trackLength)

  val wingsArray = Array.fill(2)(0: Int)
  var wingsCurrentData = wingsData
  for (i <- 0 to 1) {
    val (value, nextData) = readUByte(wingsCurrentData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }
    wingsArray(i) = value
    wingsCurrentData = nextData

    println(wingsArray(i))
  }

  val dpad = readUByte(wingsCurrentData) match {
    case Some((v, _)) => v
    case None => 0
  }
  println(dpad)
}
