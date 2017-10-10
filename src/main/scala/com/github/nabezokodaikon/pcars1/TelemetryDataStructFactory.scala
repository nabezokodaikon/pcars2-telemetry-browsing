package com.github.nabezokodaikon.pcars1

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import scala.reflect.ClassTag

object TelemetryDataStructFactory {

  def emptyArray[T: ClassTag](count: Int, emptyValue: T): Array[T] = {
    Array.fill[T](count)(emptyValue)
  }

  def createFrameInfo(data: List[Byte]): FrameInfo = {
    val frameTypeAndSequence = data(2);
    val frameType = frameTypeAndSequence & 3
    val sequence = frameTypeAndSequence >> 2
    FrameInfo(
      frameTypeAndSequence,
      frameType,
      sequence)
  }

  private def createNameString(data: List[Byte]): String = {
    val (nameString, _) = readUByteArray(data, 64) match {
      case Some((v, d)) => (toStringFromArray(v), d)
      case None => ("", List[Byte]())
    }
    nameString
  }

  def createNameStringArray(data: List[Byte], count: Int): Option[(Array[String], List[Byte])] = {
    val size = count * 64
    if (data.length < size) {
      None
    } else {
      Some(
        data.take(size).grouped(64).map(createNameString).toArray,
        data.drop(size))
    }
  }

  def createParticipantInfoStrings(data: List[Byte]): ParticipantInfoStrings = {
    val (buildVersionNumber, packetTypeData) = readUShort(data) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (packetType, carNameData) = readUByte(packetTypeData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (carName, carClassNameData) = readUByteArray(carNameData, 64) match {
      case Some((v, d)) => (toStringFromArray(v), d)
      case None => ("", Nil)
    }

    val (carClassName, trackLocationData) = readUByteArray(carClassNameData, 64) match {
      case Some((v, d)) => (toStringFromArray(v), d)
      case None => ("", Nil)
    }

    val (trackLocation, trackVariationData) = readUByteArray(trackLocationData, 64) match {
      case Some((v, d)) => (toStringFromArray(v), d)
      case None => ("", Nil)
    }

    val (trackVariation, nameStringData) = readUByteArray(trackVariationData, 64) match {
      case Some((v, d)) => (toStringFromArray(v), d)
      case None => ("", Nil)
    }

    val nameString = createNameStringArray(nameStringData, 17) match {
      case Some((v, _)) => v
      case None => emptyArray(17, "")
    }

    ParticipantInfoStrings(
      buildVersionNumber = buildVersionNumber,
      packetType = packetType,
      carName = carName,
      carClassName = carClassName,
      trackLocation = trackLocation,
      trackVariation = trackVariation,
      nameString = nameString)
  }

  val PARTICIPANT_INFO_EMPTY = ParticipantInfo(
    worldPosition = emptyArray(3, 0),
    currentLapDistance = 0,
    racePosition = 0,
    lapsCompleted = 0,
    currentLap = 0,
    sector = 0,
    lastSectorTime = 0f)

  private def createParticipantInfo(data: List[Byte]): ParticipantInfo = {

    val (worldPosition, currentLapDistanceData) = readShortArray(data, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0: Short), List[Byte]())
    }

    val (currentLapDistance, racePositionData) = readUShort(currentLapDistanceData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
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

    ParticipantInfo(
      worldPosition = worldPosition,
      currentLapDistance = currentLapDistance,
      racePosition = racePosition,
      lapsCompleted = lapsCompleted,
      currentLap = currentLap,
      sector = sector,
      lastSectorTime = lastSectorTime)
  }

  private def createParticipantInfoArray(data: List[Byte], count: Int): Option[(Array[ParticipantInfo], List[Byte])] = {
    val size = count * 16
    if (data.length < size) {
      None
    } else {
      Some(
        data.take(size).grouped(16).map(createParticipantInfo).toArray,
        data.drop(size))
    }
  }

  def createTelemetryData(data: List[Byte]): TelemetryData = {
    val (buildVersionNumber, packetTypeData) = readUShort(data) match {
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
      case None => (0: Byte, Nil)
    }

    val (numParticipants, unfilteredThrottleData) = readByte(numParticipantsData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    // Unfiltered input
    val (unfilteredThrottle, unfilteredBrakeData) = readUByteToFloat(unfilteredThrottleData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (unfilteredBrake, unfilteredSteeringData) = readUByteToFloat(unfilteredBrakeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (unfilteredSteering, unfilteredClutchData) = readByteToFloat(unfilteredSteeringData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (unfilteredClutch, raceStateFlagsData) = readUByteToFloat(unfilteredClutchData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
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

    val (currentSector1Time, currentSector2TimeData) = readFloat(currentSector1TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (currentSector2Time, currentSector3TimeData) = readFloat(currentSector2TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (currentSector3Time, fastestSector1TimeData) = readFloat(currentSector3TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (fastestSector1Time, fastestSector2TimeData) = readFloat(fastestSector1TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (fastestSector2Time, fastestSector3TimeData) = readFloat(fastestSector2TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (fastestSector3Time, personalFastestSector1TimeData) = readFloat(fastestSector3TimeData) match {
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

    val (worldFastestSector1Time, worldFastestSector2TimeData) = readFloat(worldFastestSector1TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (worldFastestSector2Time, worldFastestSector3TimeData) = readFloat(worldFastestSector2TimeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (worldFastestSector3Time, joyPad1Data) = readFloat(worldFastestSector3TimeData) match {
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
      case None => (0: Short, Nil)
    }

    val (oilPressureKPa, waterTempCelsiusData) = readUShort(oilPressureKPaData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (waterTempCelsius, waterPressureKpaData) = readShort(waterTempCelsiusData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }

    val (waterPressureKpa, fuelPressureKpaData) = readUShort(waterPressureKpaData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (fuelPressureKpa, carFlagsData) = readShort(fuelPressureKpaData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Short, Nil)
    }

    val (carFlags, fuelCapacityData) = readUByte(carFlagsData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (fuelCapacity, brakeData) = readUByte(fuelCapacityData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (brake, throttleData) = readUByteToFloat(brakeData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (throttle, clutchData) = readUByteToFloat(throttleData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (clutch, steeringData) = readUByteToFloat(clutchData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (steering, fuelLevelData) = readByteToFloat(steeringData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
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
      case None => (0: Byte, Nil)
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
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (localVelocity, worldVelocityData) = readFloatArray(localVelocityData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (worldVelocity, angularVelocityData) = readFloatArray(worldVelocityData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (angularVelocity, localAccelerationData) = readFloatArray(angularVelocityData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (localAcceleration, worldAccelerationData) = readFloatArray(localAccelerationData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (worldAcceleration, extentsCentreData) = readFloatArray(worldAccelerationData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    val (extentsCentre, tyreFlagsData) = readFloatArray(extentsCentreData, 3) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(3, 0f), List[Byte]())
    }

    // Wheels / Tyres
    val (tyreFlags, terrainData) = readUByteArray(tyreFlagsData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (terrain, tyreYData) = readUByteArray(terrainData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreY, tyreRPSData) = readFloatArray(tyreYData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (tyreRPS, tyreSlipSpeedData) = readFloatArray(tyreRPSData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (tyreSlipSpeed, tyreTempData) = readFloatArray(tyreSlipSpeedData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (tyreTemp, tyreGripData) = readUByteArray(tyreTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreGrip, tyreHeightAboveGroundData) = readUByteArray(tyreGripData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreHeightAboveGround, tyreLateralStiffnessData) = readFloatArray(tyreHeightAboveGroundData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (tyreLateralStiffness, tyreWearData) = readFloatArray(tyreLateralStiffnessData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (tyreWear, brakeDamageData) = readUByteArray(tyreWearData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (brakeDamage, suspensionDamageData) = readUByteArray(brakeDamageData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (suspensionDamage, brakeTempCelsiusData) = readUByteArray(suspensionDamageData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (brakeTempCelsius, tyreTreadTempData) = readShortArray(brakeTempCelsiusData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0: Short), List[Byte]())
    }

    val (tyreTreadTemp, tyreLayerTempData) = readUShortArray(tyreTreadTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreLayerTemp, tyreCarcassTempData) = readUShortArray(tyreLayerTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreCarcassTemp, tyreRimTempData) = readUShortArray(tyreCarcassTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreRimTemp, tyreInternalAirTempData) = readUShortArray(tyreRimTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (tyreInternalAirTemp, wheelLocalPositionYData) = readUShortArray(tyreInternalAirTempData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
    }

    val (wheelLocalPositionY, rideHeightData) = readFloatArray(wheelLocalPositionYData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (rideHeight, suspensionTravelData) = readFloatArray(rideHeightData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (suspensionTravel, suspensionVelocityData) = readFloatArray(suspensionTravelData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (suspensionVelocity, airPressureData) = readFloatArray(suspensionVelocityData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0f), List[Byte]())
    }

    val (airPressure, engineSpeedData) = readUShortArray(airPressureData, 4) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(4, 0), List[Byte]())
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
    val (aeroDamage, engineDamageData) = readUByteToFloat(aeroDamageData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (engineDamage, ambientTemperatureData) = readUByteToFloat(engineDamageData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    // Weather
    val (ambientTemperature, trackTemperatureData) = readByte(ambientTemperatureData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    val (trackTemperature, rainDensityData) = readByte(trackTemperatureData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    val (rainDensity, windSpeedData) = readUByteToFloat(rainDensityData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (windSpeed, windDirectionXData) = readByte(windSpeedData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    val (windDirectionX, windDirectionYData) = readByte(windDirectionXData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    val (windDirectionY, participantInfoData) = readByte(windDirectionYData) match {
      case Some((v, d)) => (v, d)
      case None => (0: Byte, Nil)
    }

    val (participantInfo, trackLengthData) = createParticipantInfoArray(participantInfoData, 56) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(56, PARTICIPANT_INFO_EMPTY), List[Byte]())
    }

    val (trackLength, wingsData) = readFloat(trackLengthData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (wings, dPadData) = readUByteArray(wingsData, 2) match {
      case Some((v, d)) => (v, d)
      case None => (emptyArray(2, 0), List[Byte]())
    }

    val dPad = readUByte(dPadData) match {
      case Some((v, _)) => v
      case None => 0
    }

    TelemetryData(
      buildVersionNumber = buildVersionNumber,
      packetType = packetType,

      // Game states
      gameState = gameSessionState & 7,
      sessionState = gameSessionState >> 4,
      raceStateFlags = raceStateFlags & 7,

      // Participant info
      viewedParticipantIndex = viewedParticipantIndex,
      numParticipants = numParticipants,

      // Unfiltered input
      unfilteredThrottle = unfilteredThrottle / 255f,
      unfilteredBrake = unfilteredBrake / 255f,
      unfilteredSteering = unfilteredSteering / 127f,
      unfilteredClutch = unfilteredClutch / 255f,

      // Event information
      lapsInEvent = lapsInEvent,
      trackLength = trackLength,

      // Timings
      lapInvalidated = (raceStateFlags >> 3 & 1) == 1,
      bestLapTime = bestLapTime,
      lastLapTime = lastLapTime,
      currentTime = currentTime,
      splitTimeAhead = splitTimeAhead,
      splitTimeBehind = splitTimeBehind,
      splitTime = splitTime,
      eventTimeRemaining = eventTimeRemaining,
      personalFastestLapTime = personalFastestLapTime,
      worldFastestLapTime = worldFastestLapTime,
      currentSector1Time = currentSector1Time,
      currentSector2Time = currentSector2Time,
      currentSector3Time = currentSector3Time,
      fastestSector1Time = fastestSector1Time,
      fastestSector2Time = fastestSector2Time,
      fastestSector3Time = fastestSector3Time,
      personalFastestSector1Time = personalFastestSector1Time,
      personalFastestSector2Time = personalFastestSector2Time,
      personalFastestSector3Time = personalFastestSector3Time,
      worldFastestSector1Time = worldFastestSector1Time,
      worldFastestSector2Time = worldFastestSector2Time,
      worldFastestSector3Time = worldFastestSector3Time,

      // Unused.
      joyPad1 = joyPad1,
      joyPad2 = joyPad2,

      // Flags
      highestFlagColor = highestFlag & 7,
      highestFlagReason = highestFlag >> 3 & 3,

      // Pit info
      pitMode = pitModeSchedule & 7,
      pitSchedule = pitModeSchedule >> 3 & 3,

      // Car state
      carFlags = carFlags,
      oilTempCelsius = oilTempCelsius,
      oilPressureKPa = oilPressureKPa,
      waterTempCelsius = waterTempCelsius,
      waterPressureKpa = waterPressureKpa,
      fuelPressureKpa = fuelPressureKpa,
      fuelLevel = fuelLevel,
      fuelCapacity = fuelCapacity,
      speed = speed * 3.6f,
      rpm = rpm,
      maxRpm = maxRpm,
      brake = brake / 255f,
      throttle = throttle / 255f,
      clutch = clutch / 255f,
      steering = steering / 127f,
      gear = gearNumGears & 15,
      numGears = gearNumGears >> 4,
      odometerKM = odometerKM,
      antiLockActive = (raceStateFlags >> 4 & 1) == 1,
      boostActive = (raceStateFlags >> 5 & 1) == 1,
      boostAmount = boostAmount,

      // Motion & Device Related
      orientation = orientation,
      localVelocity = localVelocity,
      worldVelocity = worldVelocity,
      angularVelocity = angularVelocity,
      localAcceleration = localAcceleration,
      worldAcceleration = worldAcceleration,
      extentsCentre = extentsCentre,

      // Wheels / Tyres
      tyreFlags = tyreFlags,
      terrain = terrain,
      tyreY = tyreY,
      tyreRPS = tyreRPS,
      tyreSlipSpeed = tyreSlipSpeed,
      tyreTemp = tyreTemp.map(_ / 255f),
      tyreGrip = tyreGrip.map(_ / 255f),
      tyreHeightAboveGround = tyreHeightAboveGround,
      tyreLateralStiffness = tyreLateralStiffness,
      tyreWear = tyreWear.map(_ / 255f),
      brakeDamage = brakeDamage.map(_ / 255f),
      suspensionDamage = suspensionDamage.map(_ / 255f),
      brakeTempCelsius = brakeTempCelsius.map(_ / 1f),
      tyreTreadTemp = tyreTreadTemp.map(_ / 1f),
      tyreLayerTemp = tyreLayerTemp.map(_ / 1f),
      tyreCarcassTemp = tyreCarcassTemp.map(_ / 1f),
      tyreRimTemp = tyreRimTemp.map(_ / 1f),
      tyreInternalAirTemp = tyreInternalAirTemp.map(_ / 1f),
      wheelLocalPositionY = wheelLocalPositionY,
      rideHeight = rideHeight,
      suspensionTravel = suspensionTravel,
      suspensionVelocity = suspensionVelocity,
      airPressure = airPressure.map(_ / 1f),

      // Extras
      engineTorque = engineTorque,
      engineSpeed = engineSpeed,
      enforcedPitStopLap = enforcedPitStopLap,

      // Car damage
      crashState = crashState,
      aeroDamage = aeroDamage / 255f,
      engineDamage = engineDamage / 255f,

      // Weather
      ambientTemperature = ambientTemperature,
      trackTemperature = trackTemperature,
      rainDensity = rainDensity / 255f,
      windSpeed = windSpeed * 2,
      windDirectionX = windDirectionX / 127f,
      windDirectionY = windDirectionY / 127f,

      participantInfo = participantInfo,

      // Unused.
      wings = wings,
      dPad = dPad)
  }
}
