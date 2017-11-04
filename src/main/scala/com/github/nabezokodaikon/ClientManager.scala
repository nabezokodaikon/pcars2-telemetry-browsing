package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.pcars2.{
  UdpData,
  RaceData,
  ParticipantsData,
  ParticipantVehicleNamesData,
  VehicleClassNamesData
}
import com.typesafe.scalalogging.LazyLogging

object ClientManager {
  case class AddClient(client: ActorRef)
  case class RemoveClient(client: ActorRef)
}

class ClientManager extends Actor with LazyLogging {
  import ClientManager._

  case class UdpDataStorage(
      raceData: Option[RaceData],
      participantsData: Option[ParticipantsData],
      participantVehicleNamesData: Option[ParticipantVehicleNamesData],
      vehicleClassNamesData: Option[VehicleClassNamesData]
  )

  override def preStart() = {
    logger.debug("ClientManager preStart.");
  }

  override def postStop() = {
    logger.debug("ClientManager postStop.")
  }

  def receive() = processing(List[ActorRef](), UdpDataStorage(None, None, None, None))

  private def processing(clientList: List[ActorRef], storage: UdpDataStorage): Receive = {
    case AddClient(client) =>
      storage.raceData match {
        case Some(udpData) => client ! udpData.toJsonString
        case None => Unit
      }
      storage.participantsData match {
        case Some(udpData) => client ! udpData.toJsonString
        case None => Unit
      }
      storage.participantVehicleNamesData match {
        case Some(udpData) => client ! udpData.toJsonString
        case None => Unit
      }
      storage.vehicleClassNamesData match {
        case Some(udpData) =>
          client ! udpData.toJsonString
        case None => Unit
      }
      context.become(processing(clientList :+ client, storage))
    case RemoveClient(client) =>
      context.become(processing(clientList.filter(_ != client).toList, storage))
    case udpData: UdpData =>
      val json = udpData.toJsonString
      clientList.foreach(_ ! json)
      udpData match {
        case udpData: RaceData =>
          context.become(processing(clientList, UdpDataStorage(
            raceData = Some(udpData),
            participantsData = storage.participantsData,
            participantVehicleNamesData = storage.participantVehicleNamesData,
            vehicleClassNamesData = storage.vehicleClassNamesData
          )))
        case udpData: ParticipantsData =>
          context.become(processing(clientList, UdpDataStorage(
            raceData = storage.raceData,
            participantsData = Some(udpData),
            participantVehicleNamesData = storage.participantVehicleNamesData,
            vehicleClassNamesData = storage.vehicleClassNamesData
          )))
        case udpData: ParticipantVehicleNamesData =>
          context.become(processing(clientList, UdpDataStorage(
            raceData = storage.raceData,
            participantsData = storage.participantsData,
            participantVehicleNamesData = Some(udpData),
            vehicleClassNamesData = storage.vehicleClassNamesData
          )))
        case udpData: VehicleClassNamesData =>
          context.become(processing(clientList, UdpDataStorage(
            raceData = storage.raceData,
            participantsData = storage.participantsData,
            participantVehicleNamesData = storage.participantVehicleNamesData,
            vehicleClassNamesData = Some(udpData)
          )))
        case _ => Unit
      }
    case _ =>
      logger.warn("ClientManager received unknown message.")
  }
}
