package com.github.nabezokodaikon.udpListener

import com.typesafe.scalalogging.LazyLogging

final object UdpListenInterval {
  val TELEMETRY_DATA_INTERVAL = 15
  val RACE_DATA_INTERVAL = 0
  val PARTICIPANTS_DATA_INTERVAL = 0
  val TIMINGS_DATA_INTERVAL = 15
  val GAME_STATE_DATA_INTERVAL = 0
  val WEATHER_STATE_INTERVAL = 0
  val VEHICLE_NAMES_INTERVAL = 0
  val TIME_STATS_DATA_INTERVAL = 0
  val PARTICIPANT_VEHICLE_NAMES_DATA_INTERVAL = 0
  val VEHICLE_CLASS_NAMES_DATA_INTERVAL = 0
}

final case class UdpDataIntervalReader(
    telemetryDataPreviewTimestamp: Long = 0,
    raceDataPreviewTimestamp: Long = 0,
    participantsDataPreviewTimestamp: Long = 0,
    timingsDataPreviewTimestamp: Long = 0,
    gameStateDataPreviewTimestamp: Long = 0,
    weatherStatePreviewTimestamp: Long = 0,
    vehicleNamesPreviewTimestamp: Long = 0,
    timeStatsDataPreviewTimestamp: Long = 0,
    participantVehicleNamesDataPreviewTimestamp: Long = 0,
    vehicleClassNamesDataPreviewTimestamp: Long = 0
) extends LazyLogging {

  def read(dataArray: Array[Byte]): (UdpDataIntervalReader, Option[UdpData]) = {
    import UdpStreamerPacketHandlerType._
    import PacketSize._
    import UdpDataReader._
    import UdpListenInterval._

    val currentTimeMillis = System.currentTimeMillis

    dataArray.length match {
      case length if length >= 12 =>
        val dataList = dataArray.toList
        val (base, _) = readPacketBase(dataList)
        base.packetType match {
          case CAR_PHYSICS if length == TELEMETRY_DATA =>
            (currentTimeMillis - telemetryDataPreviewTimestamp) match {
              case interval if interval > TELEMETRY_DATA_INTERVAL =>
                (nextReaderByTelemetryData(currentTimeMillis), Some(readTelemetryData(dataList)))
              case _ => (this, None)
            }
          case RACE_DEFINITION if length == RACE_DATA =>
            (currentTimeMillis - raceDataPreviewTimestamp) match {
              case interval if interval > RACE_DATA_INTERVAL =>
                (nextReaderByRaceData(currentTimeMillis), Some(readRaceData(dataList)))
              case _ => (this, None)
            }
          case PARTICIPANTS if length == PARTICIPANTS_DATA =>
            (currentTimeMillis - participantsDataPreviewTimestamp) match {
              case interval if interval > PARTICIPANTS_DATA_INTERVAL =>
                (nextReaderByParticipantsData(currentTimeMillis), Some(readParticipantsData(dataList)))
              case _ => (this, None)
            }
          case TIMINGS if length == TIMINGS_DATA =>
            (currentTimeMillis - timingsDataPreviewTimestamp) match {
              case interval if interval > TIMINGS_DATA_INTERVAL =>
                (nextReaderByTimingsData(currentTimeMillis), Some(readTimingsData(dataList)))
              case _ => (this, None)
            }
          case GAME_STATE if length == GAME_STATE_DATA =>
            (currentTimeMillis - gameStateDataPreviewTimestamp) match {
              case interval if interval > GAME_STATE_DATA_INTERVAL =>
                (nextReaderByGameStateData(currentTimeMillis), Some(readGameStateData(dataList)))
              case _ => (this, None)
            }
          case WEATHER_STATE =>
            logger.warn("UDP received unused packet type: WEATHER_STATE")
            (this, None)
          case VEHICLE_NAMES =>
            logger.warn("UDP received unused packet type: VEHICLE_NAMES")
            (this, None)
          case TIME_STATS if length == TIME_STATS_DATA =>
            (currentTimeMillis - timeStatsDataPreviewTimestamp) match {
              case interval if interval > TIME_STATS_DATA_INTERVAL =>
                (nextReaderByTimeStatsData(currentTimeMillis), Some(readTimeStatsData(dataList)))
              case _ => (this, None)
            }
          case PARTICIPANT_VEHICLE_NAMES if length == PARTICIPANT_VEHICLE_NAMES_DATA =>
            (currentTimeMillis - participantVehicleNamesDataPreviewTimestamp) match {
              case interval if interval > PARTICIPANT_VEHICLE_NAMES_DATA_INTERVAL =>
                (nextReaderByParticipantVehicleNamesData(currentTimeMillis), Some(readParticipantVehicleNamesData(dataList)))
              case _ => (this, None)
            }
          case PARTICIPANT_VEHICLE_NAMES if length == VEHICLE_CLASS_NAMES_DATA =>
            (currentTimeMillis - vehicleClassNamesDataPreviewTimestamp) match {
              case interval if interval > VEHICLE_CLASS_NAMES_DATA_INTERVAL =>
                (nextReaderByVehicleClassNamesData(currentTimeMillis), Some(readVehicleClassNamesData(dataList)))
              case _ => (this, None)
            }
          case _ =>
            logger.warn(s"UDP received unknown packeat. PacketType: ${base.packetType}, DataSize: ${length}")
            (this, None)
        }
      case _ =>
        logger.warn("UDP received unknown packeat.")
        (this, None)
    }
  }

  def nextReaderByTelemetryData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = timeStamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByRaceData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = timeStamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByParticipantsData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = timeStamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByTimingsData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timeStamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByGameStateData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = timeStamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByWeatherState(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = timeStamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByVehicleNames(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = timeStamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByTimeStatsData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByParticipantVehicleNamesData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = timeStamp,
      vehicleClassNamesDataPreviewTimestamp = vehicleClassNamesDataPreviewTimestamp
    )

  def nextReaderByVehicleClassNamesData(timeStamp: Long): UdpDataIntervalReader =
    UdpDataIntervalReader(
      telemetryDataPreviewTimestamp = telemetryDataPreviewTimestamp,
      raceDataPreviewTimestamp = raceDataPreviewTimestamp,
      participantsDataPreviewTimestamp = participantsDataPreviewTimestamp,
      timingsDataPreviewTimestamp = timingsDataPreviewTimestamp,
      gameStateDataPreviewTimestamp = gameStateDataPreviewTimestamp,
      weatherStatePreviewTimestamp = weatherStatePreviewTimestamp,
      vehicleNamesPreviewTimestamp = vehicleNamesPreviewTimestamp,
      timeStatsDataPreviewTimestamp = timeStatsDataPreviewTimestamp,
      participantVehicleNamesDataPreviewTimestamp = participantVehicleNamesDataPreviewTimestamp,
      vehicleClassNamesDataPreviewTimestamp = timeStamp
    )
}
