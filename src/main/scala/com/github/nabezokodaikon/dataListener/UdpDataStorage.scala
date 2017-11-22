package com.github.nabezokodaikon.dataListener

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.udpListener.{
  UdpData,
  RaceData,
  ParticipantsData,
  ParticipantVehicleNamesData,
  VehicleClassNamesData,
  LapTimeDetails,
  AggregateTime,
  FuelData
}
import com.typesafe.scalalogging.LazyLogging

final case class Storage(
    raceData: Option[(RaceData, String)],
    participantsData: Option[(ParticipantsData, String)],
    participantVehicleNamesData: Option[(ParticipantVehicleNamesData, String)],
    vehicleClassNamesData: Option[(VehicleClassNamesData, String)],
    lapTimeDetails: Option[(LapTimeDetails, String)],
    aggregateTime: Option[(AggregateTime, String)],
    fuelData: Option[(FuelData, String)]
)

final class UdpDataStorage()
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug(s"UdpDataStorage preStart.");
  }

  override def postStop() = {
    logger.debug(s"UdpDataStorage postStop.")
  }

  def receive: Receive = processing(Storage(
    raceData = None,
    participantsData = None,
    participantVehicleNamesData = None,
    vehicleClassNamesData = None,
    lapTimeDetails = None,
    aggregateTime = None,
    fuelData = None
  ))

  def processing(storage: Storage): Receive = {
    case (udpData: UdpData, json: String) => udpData match {
      case udpData: RaceData =>
        context.become(processing(Storage(
          raceData = Some((udpData, json)),
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData
        )))
      case udpData: ParticipantsData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = Some((udpData, json)),
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData
        )))
      case udpData: ParticipantVehicleNamesData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = Some((udpData, json)),
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData
        )))
      case udpData: VehicleClassNamesData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = Some((udpData, json)),
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData
        )))
      case udpData: LapTimeDetails =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = Some((udpData, json)),
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData
        )))
      case udpData: AggregateTime =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = Some((udpData, json)),
          fuelData = storage.fuelData
        )))
      case udpData: FuelData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          aggregateTime = storage.aggregateTime,
          fuelData = Some((udpData, json))
        )))
      case _ => Unit
    }
    case client: ActorRef =>
      for ((_, json) <- storage.raceData) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.participantsData) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.participantVehicleNamesData) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.vehicleClassNamesData) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.lapTimeDetails) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.aggregateTime) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.fuelData) client ! json
    case _ =>
      logger.warn(s"UdpDataStorage received unknown message.")
  }
}
