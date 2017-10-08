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
  val (buildVersionNumber, packetTypeData) = readUShort(pcarsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (packetType, gameSessionStateData) = readUByte(packetTypeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Game states
  val (gameSessionState, viewedParticipantIndexData) = readUByte(gameSessionStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Participant info
  val (viewedParticipantIndex, numParticipantsData) = readByte(viewedParticipantIndexData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (numParticipants, unfilteredThrottleData) = readByte(numParticipantsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Unfiltered input
  val (unfilteredThrottle, unfilteredBrakeData) = readUByte(unfilteredThrottleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (unfilteredBrake, unfilteredSteeringData) = readUByte(unfilteredBrakeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (unfilteredSteering, unfilteredClutchData) = readByte(unfilteredSteeringData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (unfilteredClutch, raceStateFlagsData) = readUByte(unfilteredClutchData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (raceStateFlags, lapsInEventData) = readUByte(raceStateFlagsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Event information
  val (lapsInEvent, bestLapTimeData) = readUByte(lapsInEventData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Timings
  val (bestLapTime, lastLapTimeData) = readFloat(bestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (lastLapTime, currentTimeData) = readFloat(lastLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (currentTime, splitTimeAheadData) = readFloat(currentTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (splitTimeAhead, splitTimeBehindData) = readFloat(splitTimeAheadData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (splitTimeBehind, splitTimeData) = readFloat(splitTimeBehindData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (splitTime, eventTimeRemainingData) = readFloat(splitTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (eventTimeRemaining, personalFastestLapTimeData) = readFloat(eventTimeRemainingData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (personalFastestLapTime, worldFastestLapTimeData) = readFloat(personalFastestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (worldFastestLapTime, currentSector1TimeData) = readFloat(worldFastestLapTimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (currentSector1, currentSector2TimeData) = readFloat(currentSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (currentSector2, currentSector3TimeData) = readFloat(currentSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (currentSector3, fastestSector1TimeData) = readFloat(currentSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (fastestSector1, fastestSector2TimeData) = readFloat(fastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (fastestSector2, fastestSector3TimeData) = readFloat(fastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (fastestSector3, personalFastestSector1TimeData) = readFloat(fastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (personalFastestSector1Time, personalFastestSector2TimeData) = readFloat(personalFastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (personalFastestSector2Time, personalFastestSector3TimeData) = readFloat(personalFastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (personalFastestSector3Time, worldFastestSector1TimeData) = readFloat(personalFastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (worldFastestSector1, worldFastestSector2TimeData) = readFloat(worldFastestSector1TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (worldFastestSector2, worldFastestSector3TimeData) = readFloat(worldFastestSector2TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (worldFastestSector3, joyPad1Data) = readFloat(worldFastestSector3TimeData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (joyPad1, joyPad2Data) = readUByte(joyPad1Data) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (joyPad2, highestFlagData) = readUByte(joyPad2Data) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Flags
  val (highestFlag, pitModeScheduleData) = readUByte(highestFlagData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Pit info
  val (pitModeSchedule, oilTempCelsiusData) = readUByte(pitModeScheduleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Car state
  val (oilTempCelsius, oilPressureKPaData) = readShort(oilTempCelsiusData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (oilPressureKPa, waterTempCelsiusData) = readUShort(oilPressureKPaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (waterTempCelsius, waterPressureKpaData) = readShort(waterTempCelsiusData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (waterPressureKpa, fuelPressureKpaData) = readUShort(waterPressureKpaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (fuelPressureKpa, carFlagsData) = readUShort(fuelPressureKpaData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (carFlags, fuelCapacityData) = readUByte(carFlagsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (fuelCapacity, brakeData) = readUByte(fuelCapacityData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (brake, throttleData) = readUByte(brakeData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (throttle, clutchData) = readUByte(throttleData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (clutch, steeringData) = readUByte(clutchData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (steering, fuelLevelData) = readByte(steeringData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (fuelLevel, speedData) = readFloat(fuelLevelData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (speed, rpmData) = readFloat(speedData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (rpm, maxRpmData) = readUShort(rpmData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (maxRpm, gearNumGearsData) = readUShort(maxRpmData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (gearNumGears, boostAmountData) = readUByte(gearNumGearsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (boostAmount, enforcedPitStopLapData) = readUByte(boostAmountData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (enforcedPitStopLap, crashStateData) = readByte(enforcedPitStopLapData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (crashState, odometerKMData) = readUByte(crashStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (odometerKM, orientationData) = readFloat(odometerKMData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (orientation, localVelocityData) = readFloatArray(orientationData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (localVelocity, worldVelocityData) = readFloatArray(localVelocityData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (worldVelocity, angularVelocityData) = readFloatArray(worldVelocityData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (angularVelocity, localAccelerationData) = readFloatArray(angularVelocityData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (localAcceleration, worldAccelerationData) = readFloatArray(localAccelerationData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (worldAcceleration, extentsCentreData) = readFloatArray(worldAccelerationData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (extentsCentre, tyreFlagsData) = readFloatArray(extentsCentreData, 3) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  // Wheels / Tyres
  val (tyreFlags, terrainData) = readUByteArray(tyreFlagsData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (terrain, tyreYData) = readUByteArray(terrainData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreY, tyreRPSData) = readFloatArray(tyreYData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (tyreRPS, tyreSlipSpeedData) = readFloatArray(tyreRPSData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (tyreSlipSpeed, tyreTempData) = readFloatArray(tyreSlipSpeedData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (tyreTemp, tyreGripData) = readUByteArray(tyreTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreGrip, tyreHeightAboveGroundData) = readUByteArray(tyreGripData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreHeightAboveGround, tyreLateralStiffnessData) = readFloatArray(tyreHeightAboveGroundData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (tyreLateralStiffness, tyreWearData) = readFloatArray(tyreLateralStiffnessData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (tyreWear, brakeDamageData) = readUByteArray(tyreWearData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (brakeDamage, suspensionDamageData) = readUByteArray(brakeDamageData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (suspensionDamage, brakeTempCelsiusData) = readUByteArray(suspensionDamageData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (brakeTempCelsius, tyreTreadTempData) = readShortArray(brakeTempCelsiusData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Short](), List[Byte]())
  }

  val (tyreTreadTemp, tyreLayerTempData) = readUShortArray(tyreTreadTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreLayerTemp, tyreCarcassTempData) = readUShortArray(tyreLayerTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreCarcassTemp, tyreRimTempData) = readUShortArray(tyreCarcassTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreRimTemp, tyreInternalAirTempData) = readUShortArray(tyreRimTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (tyreInternalAirTemp, wheelLocalPositionYData) = readUShortArray(tyreInternalAirTempData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  val (wheelLocalPositionY, rideHeightData) = readFloatArray(wheelLocalPositionYData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (rideHeight, suspensionTravelData) = readFloatArray(rideHeightData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (suspensionTravel, suspensionVelocityData) = readFloatArray(suspensionTravelData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (suspensionVelocity, airPressureData) = readFloatArray(suspensionVelocityData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Float](), List[Byte]())
  }

  val (airPressure, engineSpeedData) = readUShortArray(airPressureData, 4) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  // Extras
  val (engineSpeed, engineTorqueData) = readFloat(engineSpeedData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (engineTorque, aeroDamageData) = readFloat(engineTorqueData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  // Car damage
  val (aeroDamage, engineDamageData) = readUByte(aeroDamageData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (engineDamage, ambientTemperatureData) = readUByte(engineDamageData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  // Weather
  val (ambientTemperature, trackTemperatureData) = readByte(ambientTemperatureData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (trackTemperature, rainDensityData) = readByte(trackTemperatureData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (rainDensity, windSpeedData) = readUByte(rainDensityData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (windSpeed, windDirectionXData) = readByte(windSpeedData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (windDirectionX, windDirectionYData) = readByte(windDirectionXData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (windDirectionY, participantInfoData) = readByte(windDirectionYData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }

  val (participantInfo, trackLengthData) = readParticipantInfoArray(participantInfoData, 56) match {
    case Some((v, d)) => (v, d)
    case None => (Array[ParticipantInfo](), List[Byte]())
  }

  val (trackLength, wingsData) = readFloat(trackLengthData) match {
    case Some((v, d)) => (v, d)
    case None => (0f, Nil)
  }

  val (wings, dPadData) = readUByteArray(wingsData, 2) match {
    case Some((v, d)) => (v, d)
    case None => (Array[Int](), List[Byte]())
  }

  println(dPadData.size)

  val dpad = readUByte(dPadData) match {
    case Some((v, _)) => v
    case None => 0
  }
}
