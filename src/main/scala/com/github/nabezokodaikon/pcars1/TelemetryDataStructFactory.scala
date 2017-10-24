package com.github.nabezokodaikon.pcars1

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.TelemetryDataConst._
import com.github.nabezokodaikon.util.BigDecimalSupport._
import scala.reflect.ClassTag

object TelemetryDataStructFactory {

  private def emptyArray[T: ClassTag](count: Int, emptyValue: T): Array[T] = {
    Array.fill[T](count)(emptyValue)
  }

  private def toGearString(gear: Int): String =
    gear match {
      case 15 => GEAR_REVERS
      case 0 => GEAR_NEUTRAL
      case _ => gear.toString
    }

  private def createNameString(data: List[Byte], stringLength: Int): String = {
    val (nameString, _) = readUByteArray(data, stringLength) match {
      case (v, d) => (toStringFromArray(v), d)
      case _ => ("", List[Byte]())
    }
    nameString
  }

  private def createNameStringArray(data: List[Byte], stringCount: Int, stringLength: Int): (Array[String], List[Byte]) =
    stringCount * stringLength match {
      case len if len <= data.length =>
        (data.take(len).grouped(stringLength).map(d => createNameString(d, stringLength)).toArray, data.drop(len))
      case _ => (Array[String](), List[Byte]())
    }

  def createParticipantInfoStrings(data: List[Byte]): ParticipantInfoStrings = {
    val (buildVersionNumber, packetTypeData) = readUShort(data)
    val (packetType, carNameData) = readUByte(packetTypeData)

    val (carName, carClassNameData) = readUByteArray(carNameData, 64) match {
      case (v, d) => (toStringFromArray(v), d)
      case _ => ("", List[Byte]())
    }

    val (carClassName, trackLocationData) = readUByteArray(carClassNameData, 64) match {
      case (v, d) => (toStringFromArray(v), d)
      case _ => ("", List[Byte]())
    }

    val (trackLocation, trackVariationData) = readUByteArray(trackLocationData, 64) match {
      case (v, d) => (toStringFromArray(v), d)
      case _ => ("", List[Byte]())
    }

    val (trackVariation, nameStringData) = readUByteArray(trackVariationData, 64) match {
      case (v, d) => (toStringFromArray(v), d)
      case _ => ("", List[Byte]())
    }

    val nameString = createNameStringArray(nameStringData, 17, 64) match {
      case (v, d) => v
      case _ => Array[String]()
    }

    ParticipantInfoStrings(
      buildVersionNumber = buildVersionNumber,
      packetType = packetType,
      carName = carName,
      carClassName = carClassName,
      trackLocation = trackLocation,
      trackVariation = trackVariation,
      nameString = nameString //
    )
  }

  private def createParticipantInfo(data: List[Byte]): ParticipantInfo = {
    val (worldPosition, currentLapDistanceData) = readShortArray(data, 3)
    val (currentLapDistance, racePositionData) = readUShort(currentLapDistanceData)
    val (racePosition, lapsCompletedData) = readUByte(racePositionData)
    val (lapsCompleted, currentLapData) = readUByte(lapsCompletedData)
    val (currentLap, sectorData) = readUByte(currentLapData)
    val (sector, lastSectorTimeData) = readUByte(sectorData)
    val (lastSectorTime, nextData) = readFloat(lastSectorTimeData)

    ParticipantInfo(
      worldPosition = worldPosition,
      currentLapDistance = currentLapDistance,
      racePosition = racePosition & 127,
      lapsCompleted = lapsCompleted,
      currentLap = currentLap,
      sector = sector,
      lastSectorTime = lastSectorTime.toTimeFormatFromSeconds //
    )
  }

  private def createParticipantInfoArray(data: List[Byte], count: Int): (Array[ParticipantInfo], List[Byte]) =
    count * 16 match {
      case len if len <= data.length =>
        (data.take(len).grouped(16).map(createParticipantInfo).toArray, data.drop(len))
      case _ =>
        (Array[ParticipantInfo](), List[Byte]())
    }

  def createTelemetryData(data: List[Byte]): TelemetryData = {
    val (buildVersionNumber, packetTypeData) = readUShort(data)
    val (packetType, gameSessionStateData) = readUByte(packetTypeData)

    // Game states
    val (gameSessionState, viewedParticipantIndexData) = readUByte(gameSessionStateData)

    // Participant info
    val (viewedParticipantIndex, numParticipantsData) = readByte(viewedParticipantIndexData)
    val (numParticipants, unfilteredThrottleData) = readByte(numParticipantsData)

    // Unfiltered input
    val (unfilteredThrottle, unfilteredBrakeData) = readUByte(unfilteredThrottleData)
    val (unfilteredBrake, unfilteredSteeringData) = readUByte(unfilteredBrakeData)
    val (unfilteredSteering, unfilteredClutchData) = readByte(unfilteredSteeringData)
    val (unfilteredClutch, raceStateFlagsData) = readUByte(unfilteredClutchData)
    val (raceStateFlags, lapsInEventData) = readUByte(raceStateFlagsData)

    // Event information
    val (lapsInEvent, bestLapTimeData) = readUByte(lapsInEventData)

    // Timings
    val (bestLapTime, lastLapTimeData) = readFloat(bestLapTimeData)
    val (lastLapTime, currentTimeData) = readFloat(lastLapTimeData)
    val (currentTime, splitTimeAheadData) = readFloat(currentTimeData)
    val (splitTimeAhead, splitTimeBehindData) = readFloat(splitTimeAheadData)
    val (splitTimeBehind, splitTimeData) = readFloat(splitTimeBehindData)
    val (splitTime, eventTimeRemainingData) = readFloat(splitTimeData)
    val (eventTimeRemaining, personalFastestLapTimeData) = readFloat(eventTimeRemainingData)
    val (personalFastestLapTime, worldFastestLapTimeData) = readFloat(personalFastestLapTimeData)
    val (worldFastestLapTime, currentSector1TimeData) = readFloat(worldFastestLapTimeData)
    val (currentSector1Time, currentSector2TimeData) = readFloat(currentSector1TimeData)
    val (currentSector2Time, currentSector3TimeData) = readFloat(currentSector2TimeData)
    val (currentSector3Time, fastestSector1TimeData) = readFloat(currentSector3TimeData)
    val (fastestSector1Time, fastestSector2TimeData) = readFloat(fastestSector1TimeData)
    val (fastestSector2Time, fastestSector3TimeData) = readFloat(fastestSector2TimeData)
    val (fastestSector3Time, personalFastestSector1TimeData) = readFloat(fastestSector3TimeData)
    val (personalFastestSector1Time, personalFastestSector2TimeData) = readFloat(personalFastestSector1TimeData)
    val (personalFastestSector2Time, personalFastestSector3TimeData) = readFloat(personalFastestSector2TimeData)
    val (personalFastestSector3Time, worldFastestSector1TimeData) = readFloat(personalFastestSector3TimeData)
    val (worldFastestSector1Time, worldFastestSector2TimeData) = readFloat(worldFastestSector1TimeData)
    val (worldFastestSector2Time, worldFastestSector3TimeData) = readFloat(worldFastestSector2TimeData)
    val (worldFastestSector3Time, joyPad1Data) = readFloat(worldFastestSector3TimeData)
    val (joyPad1, joyPad2Data) = readUByte(joyPad1Data)
    val (joyPad2, highestFlagData) = readUByte(joyPad2Data)

    // Flags
    val (highestFlag, pitModeScheduleData) = readUByte(highestFlagData)

    // Pit info
    val (pitModeSchedule, oilTempCelsiusData) = readUByte(pitModeScheduleData)

    // Car state
    val (oilTempCelsius, oilPressureKPaData) = readShort(oilTempCelsiusData)
    val (oilPressureKPa, waterTempCelsiusData) = readUShort(oilPressureKPaData)
    val (waterTempCelsius, waterPressureKpaData) = readShort(waterTempCelsiusData)
    val (waterPressureKpa, fuelPressureKpaData) = readUShort(waterPressureKpaData)
    val (fuelPressureKpa, carFlagsData) = readShort(fuelPressureKpaData)
    val (carFlags, fuelCapacityData) = readUByte(carFlagsData)
    val (fuelCapacity, brakeData) = readUByte(fuelCapacityData)
    val (brake, throttleData) = readUByte(brakeData)
    val (throttle, clutchData) = readUByte(throttleData)
    val (clutch, steeringData) = readUByte(clutchData)
    val (steering, fuelLevelData) = readByte(steeringData)
    val (fuelLevel, speedData) = readFloat(fuelLevelData)
    val (speed, rpmData) = readFloat(speedData)
    val (rpm, maxRpmData) = readUShort(rpmData)
    val (maxRpm, gearNumGearsData) = readUShort(maxRpmData)
    val (gearNumGears, boostAmountData) = readUByte(gearNumGearsData)
    val (boostAmount, enforcedPitStopLapData) = readUByte(boostAmountData)
    val (enforcedPitStopLap, crashStateData) = readByte(enforcedPitStopLapData)
    val (crashState, odometerKMData) = readUByte(crashStateData)
    val (odometerKM, orientationData) = readFloat(odometerKMData)
    val (orientation, localVelocityData) = readFloatArray(orientationData, 3)
    val (localVelocity, worldVelocityData) = readFloatArray(localVelocityData, 3)
    val (worldVelocity, angularVelocityData) = readFloatArray(worldVelocityData, 3)
    val (angularVelocity, localAccelerationData) = readFloatArray(angularVelocityData, 3)
    val (localAcceleration, worldAccelerationData) = readFloatArray(localAccelerationData, 3)
    val (worldAcceleration, extentsCentreData) = readFloatArray(worldAccelerationData, 3)
    val (extentsCentre, tyreFlagData) = readFloatArray(extentsCentreData, 3)

    // Wheels / Tyres
    val (tyreFlag, terrainData) = readUByteArray(tyreFlagData, 4)
    val (terrain, tyreYData) = readUByteArray(terrainData, 4)
    val (tyreY, tyreRPSData) = readFloatArray(tyreYData, 4)
    val (tyreRPS, tyreSlipSpeedData) = readFloatArray(tyreRPSData, 4)
    val (tyreSlipSpeed, tyreTempData) = readFloatArray(tyreSlipSpeedData, 4)
    val (tyreTemp, tyreGripData) = readUByteArray(tyreTempData, 4)
    val (tyreGrip, tyreHeightAboveGroundData) = readUByteArray(tyreGripData, 4)
    val (tyreHeightAboveGround, tyreLateralStiffnessData) = readFloatArray(tyreHeightAboveGroundData, 4)
    val (tyreLateralStiffness, tyreWearData) = readFloatArray(tyreLateralStiffnessData, 4)
    val (tyreWear, brakeDamageData) = readUByteArray(tyreWearData, 4)
    val (brakeDamage, suspensionDamageData) = readUByteArray(brakeDamageData, 4)
    val (suspensionDamage, brakeTempCelsiusData) = readUByteArray(suspensionDamageData, 4)
    val (brakeTempCelsius, tyreTreadTempData) = readShortArray(brakeTempCelsiusData, 4)
    val (tyreTreadTemp, tyreLayerTempData) = readUShortArray(tyreTreadTempData, 4)
    val (tyreLayerTemp, tyreCarcassTempData) = readUShortArray(tyreLayerTempData, 4)
    val (tyreCarcassTemp, tyreRimTempData) = readUShortArray(tyreCarcassTempData, 4)
    val (tyreRimTemp, tyreInternalAirTempData) = readUShortArray(tyreRimTempData, 4)
    val (tyreInternalAirTemp, wheelLocalPositionYData) = readUShortArray(tyreInternalAirTempData, 4)
    val (wheelLocalPositionY, rideHeightData) = readFloatArray(wheelLocalPositionYData, 4)
    val (rideHeight, suspensionTravelData) = readFloatArray(rideHeightData, 4)
    val (suspensionTravel, suspensionVelocityData) = readFloatArray(suspensionTravelData, 4)
    val (suspensionVelocity, airPressureData) = readFloatArray(suspensionVelocityData, 4)
    val (airPressure, engineSpeedData) = readUShortArray(airPressureData, 4)

    // Extras
    val (engineSpeed, engineTorqueData) = readFloat(engineSpeedData)
    val (engineTorque, aeroDamageData) = readFloat(engineTorqueData)

    // Car damage
    val (aeroDamage, engineDamageData) = readUByte(aeroDamageData)
    val (engineDamage, ambientTemperatureData) = readUByte(engineDamageData)

    // Weather
    val (ambientTemperature, trackTemperatureData) = readByte(ambientTemperatureData)
    val (trackTemperature, rainDensityData) = readByte(trackTemperatureData)
    val (rainDensity, windSpeedData) = readUByte(rainDensityData)
    val (windSpeed, windDirectionXData) = readByte(windSpeedData)
    val (windDirectionX, windDirectionYData) = readByte(windDirectionXData)
    val (windDirectionY, participantInfoData) = readByte(windDirectionYData)
    val (participantInfo, trackLengthData) = createParticipantInfoArray(participantInfoData, 56)
    val (trackLength, wingsData) = readFloat(trackLengthData)
    val (wings, dPadData) = readUByteArray(wingsData, 2)
    val dPad = readUByte(dPadData)

    TelemetryData(
      gameStateData = GameStateData(
        gameState = gameSessionState & 7,
        sessionState = gameSessionState >> 4,
        raceStateFlags = raceStateFlags & 7 //
      ),
      participantInfoData = ParticipantInfoData(
        viewedParticipantIndex = viewedParticipantIndex,
        numParticipants = numParticipants //
      ),
      participantInfo = participantInfo,
      unfilteredInputData = UnfilteredInputData(
        unfilteredThrottle = unfilteredThrottle.divide(255, 2),
        unfilteredBrake = unfilteredBrake.divide(255, 2),
        unfilteredSteering = unfilteredSteering.divide(127, 2),
        unfilteredClutch = unfilteredClutch.divide(255, 2) //
      ),
      eventInfoData = EventInfoData(
        lapsInEvent = lapsInEvent,
        trackLength = trackLength.divide(1000, 3) //
      ),
      timingInfoData = TimingInfoData(
        lapInvalidated = (raceStateFlags >> 3 & 1) == 1,
        bestLapTime = bestLapTime.toTimeFormatFromSeconds,
        lastLapTime = lastLapTime.toTimeFormatFromSeconds,
        currentTime = currentTime.toTimeFormatFromSeconds,
        splitTimeAhead = splitTimeAhead.toTimeFormatFromSeconds,
        splitTimeBehind = splitTimeBehind.toTimeFormatFromSeconds,
        splitTime = splitTime.toTimeFormatFromSeconds,
        eventTimeRemaining = eventTimeRemaining.toTimeFormatFromMilliseconds,
        personalFastestLapTime = personalFastestLapTime.toTimeFormatFromSeconds,
        worldFastestLapTime = worldFastestLapTime.toTimeFormatFromSeconds //
      ),
      sectorTimeData = SectorTimeData(
        currentSector1Time = currentSector1Time.toTimeFormatFromSeconds,
        currentSector2Time = currentSector2Time.toTimeFormatFromSeconds,
        currentSector3Time = currentSector3Time.toTimeFormatFromSeconds,
        fastestSector1Time = fastestSector1Time.toTimeFormatFromSeconds,
        fastestSector2Time = fastestSector2Time.toTimeFormatFromSeconds,
        fastestSector3Time = fastestSector3Time.toTimeFormatFromSeconds,
        personalFastestSector1Time = personalFastestSector1Time.toTimeFormatFromSeconds,
        personalFastestSector2Time = personalFastestSector2Time.toTimeFormatFromSeconds,
        personalFastestSector3Time = personalFastestSector3Time.toTimeFormatFromSeconds,
        worldFastestSector1Time = worldFastestSector1Time.toTimeFormatFromSeconds,
        worldFastestSector2Time = worldFastestSector2Time.toTimeFormatFromSeconds,
        worldFastestSector3Time = worldFastestSector3Time.toTimeFormatFromSeconds //
      ),
      flagData = FlagData(
        highestFlagColor = highestFlag & 7,
        highestFlagReason = highestFlag >> 3 & 3 //
      ),
      pitInfoData = PitInfoData(
        pitMode = pitModeSchedule & 7,
        pitSchedule = pitModeSchedule >> 3 & 3 //
      ),
      carStateData = CarStateData(
        oilTempCelsius = oilTempCelsius.divide(255, 0),
        oilPressureKPa = oilPressureKPa,
        waterTempCelsius = waterTempCelsius.divide(255, 0),
        waterPressureKpa = waterPressureKpa,
        fuelPressureKpa = fuelPressureKpa,
        carFlags = carFlags,
        fuelCapacity = fuelCapacity,
        brake = brake.divide(255, 2),
        throttle = throttle.divide(255, 2),
        clutch = clutch.divide(255, 2),
        steering = steering.divide(127, 2),
        fuelLevel = fuelLevel.multiply(fuelCapacity, 1),
        speed = speed.multiply(3.6, 0),
        rpm = rpm,
        maxRpm = maxRpm,
        gear = toGearString(gearNumGears & 15),
        numGears = gearNumGears >> 4,
        boostAmount = boostAmount,
        enforcedPitStopLap = enforcedPitStopLap,
        odometerKM = odometerKM.toRound(3),
        antiLockActive = (raceStateFlags >> 4 & 1) == 1,
        boostActive = (raceStateFlags >> 5 & 1) == 1 //
      ),
      carStateVecotrData = CarStateVecotrData(
        orientation = orientation,
        localVelocity = localVelocity,
        worldVelocity = worldVelocity,
        angularVelocity = angularVelocity,
        localAcceleration = localAcceleration,
        worldAcceleration = worldAcceleration,
        extentsCentre = extentsCentre //
      ),
      tyreData = TyreData(
        tyreFlag = tyreFlag,
        terrain = terrain,
        tyreY = tyreY,
        tyreRPS = tyreRPS,
        tyreSlipSpeed = tyreSlipSpeed,
        tyreTemp = tyreTemp,
        tyreGrip = tyreGrip.map(_.divide(255f, 2)),
        tyreHeightAboveGround = tyreHeightAboveGround,
        tyreLateralStiffness = tyreLateralStiffness,
        tyreWear = tyreWear.map(_.divide(255f, 2)),
        brakeDamage = brakeDamage.map(_.divide(255f, 2)),
        suspensionDamage = suspensionDamage.map(_.divide(255f, 2)),
        brakeTempCelsius = brakeTempCelsius.map(_.divide(255f, 0)),
        tyreTreadTemp = tyreTreadTemp.map(_ / 1f),
        tyreLayerTemp = tyreLayerTemp.map(_ / 1f),
        tyreCarcassTemp = tyreCarcassTemp.map(_ / 1f),
        tyreRimTemp = tyreRimTemp.map(_ / 1f),
        tyreInternalAirTemp = tyreInternalAirTemp.map(_ / 1f) //
      ),
      tyreUdpData = TyreUdpData(
        wheelLocalPositionY = wheelLocalPositionY,
        rideHeight = rideHeight.map(_.multiply(100, 1)),
        suspensionTravel = suspensionTravel.map(_.multiply(100, 1)),
        suspensionVelocity = suspensionVelocity,
        airPressure = airPressure.map(_.divide(100, 2)) //
      ),
      otherUdpData = OtherUdpData(
        engineSpeed = engineSpeed,
        engineTorque = engineTorque //
      ),
      carDamageData = CarDamageData(
        crashState = crashState,
        aeroDamage = aeroDamage.divide(255, 2),
        engineDamage = engineDamage.divide(255, 2) //
      ),
      weatherData = WeatherData(
        ambientTemperature = ambientTemperature,
        trackTemperature = trackTemperature,
        rainDensity = rainDensity.divide(255, 2),
        windSpeed = windSpeed * 2,
        windDirectionX = windDirectionX.divide(127, 2),
        windDirectionY = windDirectionY.divide(127, 2) //
      ) //
    )
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

  def getJsonText(dataArray: Array[Byte]): String = {
    if (dataArray.length < 3) {
      ""
    } else {
      val dataList = dataArray.toList
      val frameInfo = createFrameInfo(dataList)
      frameInfo.frameType match {
        case TELEMETRY_DATA_FRAME_TYPE => createTelemetryData(dataList).toJsonString
        case PARTICIPANT_INFO_STRINGS_FRAME_TYPE => createParticipantInfoStrings(dataList).toJsonString
        case PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE => ""
        case _ => ""
      }
    }
  }
}
