package com.github.nabezokodaikon.pcars2

import com.github.nabezokodaikon.pcars2._
import com.github.nabezokodaikon.pcars2.UDPDataConst._
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.github.nabezokodaikon.util.BinaryUtil._
import scala.reflect.ClassTag

object UdpDataReader {

  private def readDefineArray[T: ClassTag](func: List[Byte] => (T, List[Byte]), data: List[Byte], dataCount: Int, dataLength: Int): (Array[T], List[Byte]) =
    dataCount * dataLength match {
      case len if len <= data.length =>
        (data.take(len).grouped(dataLength).map(d => func(d)._1).toArray, data.drop(len))
      case _ => (Array[T](), List[Byte]())
    }

  def readPacketBase(data1: List[Byte]): (PacketBase, List[Byte]) = {
    val (packetNumber, data2) = readUInt(data1)
    val (categoryPacketNumber, data3) = readUInt(data2)
    val (partialPacketIndex, data4) = readUByte(data3)
    val (partialPackatNumber, data5) = readUByte(data4)
    val (packetType, data6) = readUByte(data5)
    val (packetVersion, nextData) = readUByte(data6)

    (
      PacketBase(
        packetNumber = packetNumber,
        categoryPacketNumber = categoryPacketNumber,
        partialPacketIndex = partialPacketIndex,
        partialPacketNumber = partialPackatNumber,
        packetType = packetType,
        packetVersion = packetVersion
      ),
      nextData
    )
  }

  private def readTelemetryParticipantInfo(data1: List[Byte]): (TelemetryParticipantInfo, List[Byte]) = {
    val (viewedParticipantIndex, nextData) = readByte(data1)

    (
      TelemetryParticipantInfo(
        viewedParticipantIndex = viewedParticipantIndex
      ),
      nextData
    )
  }

  private def readUnfilteredInput(data1: List[Byte]): (UnfilteredInput, List[Byte]) = {
    val (unfilteredThrottle, data2) = readUByte(data1)
    val (unfilteredBrake, data3) = readUByte(data2)
    val (unfilteredSteering, data4) = readByte(data3)
    val (unfilteredClutch, nextData) = readUByte(data4)

    (
      UnfilteredInput(
        unfilteredThrottle = unfilteredThrottle,
        unfilteredBrake = unfilteredBrake,
        unfilteredSteering = unfilteredSteering,
        unfilteredClutch = unfilteredClutch
      ),
      nextData
    )
  }

  private def readCarState(data1: List[Byte]): (CarState, List[Byte]) = {
    val (carFlags, data2) = readUByte(data1)
    val (oilTempCelsius, data3) = readShort(data2)
    val (oilPressureKPa, data4) = readUShort(data3)
    val (waterTempCelsius, data5) = readShort(data4)
    val (waterPressureKpa, data6) = readUShort(data5)
    val (fuelPressureKpa, data7) = readUShort(data6)
    val (fuelCapacity, data8) = readUByte(data7)
    val (brake, data9) = readUByte(data8)
    val (throttle, data10) = readUByte(data9)
    val (clutch, data11) = readUByte(data10)
    val (fuelLevel, data12) = readFloat(data11)
    val (speed, data13) = readFloat(data12)
    val (rpm, data14) = readUShort(data13)
    val (maxRpm, data15) = readUShort(data14)
    val (steering, data16) = readByte(data15)
    val (gearNumGears, data17) = readUByte(data16)
    val (boostAmount, data18) = readUByte(data17)
    val (crashState, data19) = readUByte(data18)
    val (odometerKM, nextData) = readFloat(data19)

    (
      CarState(
        carFlags = carFlags,
        oilTempCelsius = oilTempCelsius,
        oilPressureKPa = oilPressureKPa,
        waterTempCelsius = waterTempCelsius,
        waterPressureKpa = waterPressureKpa,
        fuelPressureKpa = fuelPressureKpa,
        fuelCapacity = fuelCapacity,
        brake = brake,
        throttle = throttle,
        clutch = clutch,
        fuelLevel = fuelLevel,
        speed = speed,
        rpm = rpm,
        maxRpm = maxRpm,
        steering = steering,
        gearNumGears = gearNumGears,
        boostAmount = boostAmount,
        crashState = crashState,
        odometerKM = odometerKM
      ),
      nextData
    )
  }

  private def readVelocity(data1: List[Byte]): (Velocity, List[Byte]) = {
    val (orientation, data2) = readFloatArray(data1, 3)
    val (localVelocity, data3) = readFloatArray(data2, 3)
    val (worldVelocity, data4) = readFloatArray(data3, 3)
    val (angularVelocity, data5) = readFloatArray(data4, 3)
    val (localAcceleration, data6) = readFloatArray(data5, 3)
    val (worldAcceleration, data7) = readFloatArray(data6, 3)
    val (extentsCentre, nextData) = readFloatArray(data7, 3)

    (
      Velocity(
        orientation = orientation,
        localVelocity = localVelocity,
        worldVelocity = worldVelocity,
        angularVelocity = angularVelocity,
        localAcceleration = localAcceleration,
        worldAcceleration = worldAcceleration,
        extentsCentre = extentsCentre
      ),
      nextData
    )
  }

  private def readTyre1(data1: List[Byte]): (Tyre1, List[Byte]) = {
    val (tyreFlags, data2) = readUByteArray(data1, 4)
    val (terrain, data3) = readUByteArray(data2, 4)
    val (tyreY, data4) = readFloatArray(data3, 4)
    val (tyreRPS, data5) = readFloatArray(data4, 4)
    val (tyreTemp, data6) = readUByteArray(data5, 4)
    val (tyreHeightAboveGround, data7) = readFloatArray(data6, 4)
    val (tyreWear, data8) = readUByteArray(data7, 4)
    val (brakeDamage, data9) = readUByteArray(data8, 4)
    val (suspensionDamage, data10) = readUByteArray(data9, 4)
    val (brakeTempCelsius, data11) = readShortArray(data10, 4)
    val (tyreTreadTemp, data12) = readUShortArray(data11, 4)
    val (tyreLayerTemp, data13) = readUShortArray(data12, 4)
    val (tyreCarcassTemp, data14) = readUShortArray(data13, 4)
    val (tyreRimTemp, data15) = readUShortArray(data14, 4)
    val (tyreInternalAirTemp, data16) = readUShortArray(data15, 4)
    val (tyreTempLeft, data17) = readUShortArray(data16, 4)
    val (tyreTempCenter, data18) = readUShortArray(data17, 4)
    val (tyreTempRight, data19) = readUShortArray(data18, 4)
    val (wheelLocalPositionY, data20) = readFloatArray(data19, 4)
    val (rideHeight, data21) = readFloatArray(data20, 4)
    val (suspensionTravel, data22) = readFloatArray(data21, 4)
    val (suspensionVelocity, nextData) = readFloatArray(data22, 4)

    (
      Tyre1(
        tyreFlags = tyreFlags,
        terrain = terrain,
        tyreY = tyreY,
        tyreRPS = tyreRPS,
        tyreTemp = tyreTemp,
        tyreHeightAboveGround = tyreHeightAboveGround,
        tyreWear = tyreWear,
        brakeDamage = brakeDamage,
        suspensionDamage = suspensionDamage,
        brakeTempCelsius = brakeTempCelsius,
        tyreTreadTemp = tyreTreadTemp,
        tyreLayerTemp = tyreLayerTemp,
        tyreCarcassTemp = tyreCarcassTemp,
        tyreRimTemp = tyreRimTemp,
        tyreInternalAirTemp = tyreInternalAirTemp,
        tyreTempLeft = tyreTempLeft,
        tyreTempCenter = tyreTempCenter,
        tyreTempRight = tyreTempRight,
        wheelLocalPositionY = wheelLocalPositionY,
        rideHeight = rideHeight,
        suspensionTravel = suspensionTravel,
        suspensionVelocity = suspensionVelocity
      ),
      nextData
    )
  }

  private def readTyre2(data1: List[Byte]): (Tyre2, List[Byte]) = {
    val (suspensionRideHeight, data2) = readUShortArray(data1, 4)
    val (airPressure, nextData) = readUShortArray(data2, 4)

    (
      Tyre2(
        suspensionRideHeight = suspensionRideHeight,
        airPressure = airPressure
      ),
      nextData
    )
  }

  private def readTyre3(data1: List[Byte]): (Tyre3, List[Byte]) = {
    val (engineSpeed, data2) = readFloat(data1)
    val (engineTorque, data3) = readFloat(data2)
    val (wings, data4) = readUByteArray(data3, 2)
    val (handBrake, nextData) = readUByte(data4)

    (
      Tyre3(
        engineSpeed = engineSpeed,
        engineTorque = engineTorque,
        wings = wings,
        handBrake = handBrake
      ),
      nextData
    )
  }

  private def readCarDamage(data1: List[Byte]): (CarDamage, List[Byte]) = {
    val (aeroDamage, data2) = readUByte(data1)
    val (engineDamage, nextData) = readUByte(data2)

    (
      CarDamage(
        aeroDamage = aeroDamage,
        engineDamage = engineDamage
      ),
      nextData
    )
  }

  private def readHWState(data1: List[Byte]): (HWState, List[Byte]) = {
    val (joyPad0, data2) = readUInt(data1)
    val (dPad, data3) = readUByte(data2)
    val (tyreCompound, nextData) = readStringArray(data3, 4, TYRE_NAME_LENGTH_MAX)

    (
      HWState(
        joyPad0 = joyPad0,
        dPad = dPad,
        tyreCompound = tyreCompound
      ),
      nextData
    )
  }

  def readTelemetryData(data1: List[Byte]): TelemetryData = {
    val (base, data2) = readPacketBase(data1)
    val (participantinfo, data3) = readTelemetryParticipantInfo(data2)
    val (unfilteredInput, data4) = readUnfilteredInput(data3)
    val (carState, data5) = readCarState(data4)
    val (velocity, data6) = readVelocity(data5)
    val (tyre1, data7) = readTyre1(data6)
    val (tyre2, data8) = readTyre2(data7)
    val (tyre3, data9) = readTyre3(data8)
    val (carDamage, data10) = readCarDamage(data9)
    val (hWState, nextData) = readHWState(data10)

    TelemetryData(
      base = base,
      participantinfo = participantinfo,
      unfilteredInput = unfilteredInput,
      carState = carState,
      velocity = velocity,
      tyre1 = tyre1,
      tyre2 = tyre2,
      tyre3 = tyre3,
      carDamage = carDamage,
      hwState = hWState
    )
  }

  def readRaceData(data1: List[Byte]): RaceData = {
    val (base, data2) = readPacketBase(data1)
    val (worldFastestLapTime, data3) = readFloat(data2)
    val (personalFastestLapTime, data4) = readFloat(data3)
    val (personalFastestSector1Time, data5) = readFloat(data4)
    val (personalFastestSector2Time, data6) = readFloat(data5)
    val (personalFastestSector3Time, data7) = readFloat(data6)
    val (worldFastestSector1Time, data8) = readFloat(data7)
    val (worldFastestSector2Time, data9) = readFloat(data8)
    val (worldFastestSector3Time, data10) = readFloat(data9)
    val (trackLength, data11) = readFloat(data10)
    val (trackLocation, data12) = readString(data11, TRACKNAME_LENGTH_MAX)
    val (trackVariation, data13) = readString(data12, TRACKNAME_LENGTH_MAX)
    val (translatedTrackLocation, data14) = readString(data13, TRACKNAME_LENGTH_MAX)
    val (translatedTrackVariation, data15) = readString(data14, TRACKNAME_LENGTH_MAX)
    val (lapsTimeInEvent, data16) = readUShort(data15)
    val (enforcedPitStopLap, nextData) = readByte(data16)

    RaceData(
      base = base,
      worldFastestLapTime = worldFastestLapTime,
      personalFastestLapTime = personalFastestLapTime,
      personalFastestSector1Time = personalFastestSector1Time,
      personalFastestSector2Time = personalFastestSector2Time,
      personalFastestSector3Time = personalFastestSector3Time,
      worldFastestSector1Time = worldFastestSector1Time,
      worldFastestSector2Time = worldFastestSector2Time,
      worldFastestSector3Time = worldFastestSector3Time,
      trackLength = trackLength,
      trackLocation = trackLocation,
      trackVariation = trackVariation,
      translatedTrackLocation = translatedTrackLocation,
      translatedTrackVariation = translatedTrackVariation,
      lapsTimeInEvent = lapsTimeInEvent,
      enforcedPitStopLap = enforcedPitStopLap
    )
  }

  def readParticipantsData(data1: List[Byte]): ParticipantsData = {
    val (base, data2) = readPacketBase(data1)
    val (participantsChangedTimestamp, data3) = readUInt(data2)
    val (name, nextData) = readStringArray(data3, PARTICIPANTS_PER_PACKET, PARTICIPANT_NAME_LENGTH_MAX)

    ParticipantsData(
      base = base,
      participantsChangedTimestamp = participantsChangedTimestamp,
      name = name
    )
  }

  private def readParticipantInfo(data1: List[Byte]): (ParticipantInfo, List[Byte]) = {
    val (worldPosition, data2) = readShortArray(data1, 3)
    val (orientation, data3) = readShortArray(data2, 3)
    val (currentLapDistance, data4) = readUShort(data3)
    val (racePosition, data5) = readUByte(data4)
    val (sector, data6) = readUByte(data5)
    val (highestFlag, data7) = readUByte(data6)
    val (pitModeSchedule, data8) = readUByte(data7)
    val (carIndex, data9) = readUShort(data8)
    val (raceState, data10) = readUByte(data9)
    val (currentLap, data11) = readUByte(data10)
    val (currentTime, data12) = readFloat(data11)
    val (currentSectorTime, nextData) = readFloat(data12)

    (
      ParticipantInfo(
        worldPosition = worldPosition,
        orientation = orientation,
        currentLapDistance = currentLapDistance,
        racePosition = racePosition,
        sector = sector,
        highestFlag = highestFlag,
        pitModeSchedule = pitModeSchedule,
        carIndex = carIndex,
        raceState = raceState,
        currentLap = currentLap,
        currentTime = currentTime,
        currentSectorTime = currentSectorTime
      ),
      nextData
    )
  }

  def readTimingsData(data1: List[Byte]): TimingsData = {
    val (base, data2) = readPacketBase(data1)
    val (numParticipants, data3) = readByte(data2)
    val (participantsChangedTimestamp, data4) = readUInt(data3)
    val (eventTimeRemaining, data5) = readFloat(data4)
    val (splitTimeAhead, data6) = readFloat(data5)
    val (splitTimeBehind, data7) = readFloat(data6)
    val (splitTime, data8) = readFloat(data7)
    val (partcipants, nextData) = readDefineArray(readParticipantInfo, data8, UDP_STREAMER_PARTICIPANTS_SUPPORTED, 30)

    TimingsData(
      base = base,
      numParticipants = numParticipants,
      participantsChangedTimestamp = participantsChangedTimestamp,
      eventTimeRemaining = eventTimeRemaining,
      splitTimeAhead = splitTimeAhead,
      splitTimeBehind = splitTimeBehind,
      splitTime = splitTime,
      partcipants = partcipants
    )
  }

  def readGameStateData(data1: List[Byte]): GameStateData = {
    val (base, data2) = readPacketBase(data1)
    val (buildVersionNumber, data3) = readUShort(data2)
    val (gameState, data4) = readByte(data3)
    val (ambientTemperature, data5) = readByte(data4)
    val (trackTemperature, data6) = readByte(data5)
    val (rainDensity, data7) = readUByte(data6)
    val (snowDensity, data8) = readUByte(data7)
    val (windSpeed, data9) = readByte(data8)
    val (windDirectionX, data10) = readByte(data9)
    val (windDirectionY, nextData) = readByte(data10)

    GameStateData(
      base = base,
      buildVersionNumber = buildVersionNumber,
      gameState = (gameState & 7).toByte,
      sessionState = (gameState >> 4).toByte,
      ambientTemperature = ambientTemperature,
      trackTemperature = trackTemperature,
      rainDensity = rainDensity,
      snowDensity = snowDensity,
      windSpeed = windSpeed,
      windDirectionX = windDirectionX,
      windDirectionY = windDirectionY
    )
  }

  def readParticipantStatsInfo(data1: List[Byte]): (ParticipantStatsInfo, List[Byte]) = {
    val (fastestLapTime, data2) = readFloat(data1)
    val (lastLapTime, data3) = readFloat(data2)
    val (lastSectorTime, data4) = readFloat(data3)
    val (fastestSector1Time, data5) = readFloat(data4)
    val (fastestSector2Time, data6) = readFloat(data5)
    val (fastestSector3Time, nextData) = readFloat(data6)

    (
      ParticipantStatsInfo(
        fastestLapTime = fastestLapTime,
        lastLapTime = lastLapTime,
        lastSectorTime = lastSectorTime,
        fastestSector1Time = fastestSector1Time,
        fastestSector2Time = fastestSector2Time,
        fastestSector3Time = fastestSector3Time
      ),
      nextData
    )
  }

  private def readParticipantsStats(data1: List[Byte]): (ParticipantsStats, List[Byte]) = {
    val (participants, nextData) = readDefineArray(readParticipantStatsInfo, data1, UDP_STREAMER_PARTICIPANTS_SUPPORTED, 24)

    (
      ParticipantsStats(
        participants = participants
      ),
      nextData
    )
  }

  def readTimeStatsData(data1: List[Byte]): TimeStatsData = {
    val (base, data2) = readPacketBase(data1)
    val (participantsChangedTimestamp, data3) = readUInt(data2)
    val (stats, nextData) = readParticipantsStats(data3)

    TimeStatsData(
      base = base,
      participantsChangedTimestamp = participantsChangedTimestamp,
      stats = stats
    )
  }

  private def readVehicleInfo(data1: List[Byte]): (VehicleInfo, List[Byte]) = {
    val (index, data2) = readUShort(data1)
    val (carClass, data3) = readUInt(data2)
    val (name, nextData) = readString(data3, VEHICLE_NAME_LENGTH_MAX)
    (
      VehicleInfo(
        index = index,
        carClass = carClass,
        name = name
      ),
      nextData
    )
  }

  def readParticipantVehicleNamesData(data1: List[Byte]): ParticipantVehicleNamesData = {
    val (base, data2) = readPacketBase(data1)
    val (vehicles, nextData) = readDefineArray(readVehicleInfo, data2, VEHICLES_PER_PACKET, 72)

    ParticipantVehicleNamesData(
      base = base,
      vehicles = vehicles
    )
  }

  private def readClassInfo(data1: List[Byte]): (ClassInfo, List[Byte]) = {
    val (classIndex, data2) = readUInt(data1)
    val (name, nextData) = readString(data2, CLASS_NAME_LENGTH_MAX)

    (
      ClassInfo(
        classIndex = classIndex,
        name = name
      ),
      nextData
    )
  }

  def readVehicleClassNamesData(data1: List[Byte]): VehicleClassNamesData = {
    val (base, data2) = readPacketBase(data1)
    val (classInfo, nextData) = readDefineArray(readClassInfo, data2, CLASSES_SUPPORTED_PER_PACKET, 24)

    VehicleClassNamesData(
      base,
      classInfo
    )
  }
}
