package com.github.nabezokodaikon.udpListener

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging

object UdpDataMerger {

  def mergeParticipantsData(
    dataList: List[ParticipantsData]
  ): Option[ParticipantsData] =
    dataList.lastOption match {
      case Some(last) =>
        val name = dataList.flatMap(data => data.name).toArray
        val nationality = dataList.flatMap(data => data.nationality).toArray
        val index = dataList.flatMap(data => data.index).toArray
        val mergeData = ParticipantsData(
          base = last.base,
          participantsChangedTimestamp = last.participantsChangedTimestamp,
          name = name,
          nationality = nationality,
          index = index
        )
        Some(mergeData)
      case None => None
    }

  def mergeParticipantVehicleNamesData(
    dataList: List[ParticipantVehicleNamesData]
  ): Option[ParticipantVehicleNamesData] =
    dataList.lastOption match {
      case Some(last) =>
        val vehicles = dataList.flatMap(data => data.vehicles).toArray
        val mergeData = ParticipantVehicleNamesData(
          base = last.base,
          vehicles = vehicles
        )
        Some(mergeData)
      case None => None
    }

  def mergeVehicleClassNamesData(
    dataList: List[VehicleClassNamesData]
  ): Option[VehicleClassNamesData] =
    dataList.lastOption match {
      case Some(last) =>
        val classes = dataList.flatMap(data => data.classes).toArray
        val mergeData = VehicleClassNamesData(
          base = last.base,
          classes = classes
        )
        Some(mergeData)
      case None => None
    }
}

trait MergeDataListener[T] extends Actor with LazyLogging {
  import Actor.Receive
  val emptyList: List[T] = List[T]()
  def receive(): Receive = processing(List[T]())
  def processing(dataList: List[T]): Receive
}

final class ParticipantsDataListener(clientManager: ActorRef)
  extends MergeDataListener[ParticipantsData] {

  override def preStart() = {
    logger.debug("ParticipantsDataListener preStart.");
  }

  override def postStop() = {
    logger.debug("ParticipantsDataListener postStop.");
  }

  def processing(dataList: List[ParticipantsData]): Receive = {
    case data: ParticipantsData =>
      val number = data.base.partialPacketNumber
      data.base.partialPacketIndex match {
        case index if index == number =>
          UdpDataMerger.mergeParticipantsData(dataList :+ data) match {
            case Some(mergeData) =>
              clientManager ! mergeData
            case None => Unit
          }
          context.become(processing(emptyList))
        case index if index < number =>
          context.become(processing(dataList :+ data))
        case _ =>
          context.become(processing(emptyList))
      }
    case _ =>
      logger.warn("ParticipantsDataListener received unknown message.")
  }
}

final class ParticipantVehicleNamesDataListener(clientManager: ActorRef)
  extends MergeDataListener[ParticipantVehicleNamesData] {

  override def preStart() = {
    logger.debug("ParticipantVehicleNamesDataListener preStart.");
  }

  override def postStop() = {
    logger.debug("ParticipantVehicleNamesDataListener postStop.");
  }

  def processing(dataList: List[ParticipantVehicleNamesData]): Receive = {
    case data: ParticipantVehicleNamesData =>
      val number = data.base.partialPacketNumber - 1
      data.base.partialPacketIndex match {
        case index if index == number =>
          UdpDataMerger.mergeParticipantVehicleNamesData(dataList :+ data) match {
            case Some(mergeData) =>
              clientManager ! mergeData
            case None => Unit
          }
          context.become(processing(emptyList))
        case index if index < number =>
          context.become(processing(dataList :+ data))
        case _ =>
          context.become(processing(emptyList))
      }
    case _ =>
      logger.warn("ParticipantVehicleNamesDataListener received unknown message.")
  }
}

final class VehicleClassNamesDataListener(clientManager: ActorRef)
  extends MergeDataListener[VehicleClassNamesData] {

  override def preStart() = {
    logger.debug("VehicleClassNamesDataListener preStart.");
  }

  override def postStop() = {
    logger.debug("VehicleClassNamesDataListener postStop.");
  }

  def processing(dataList: List[VehicleClassNamesData]): Receive = {
    case data: VehicleClassNamesData =>
      val number = data.base.partialPacketNumber
      data.base.partialPacketIndex match {
        case index if index == number =>
          UdpDataMerger.mergeVehicleClassNamesData(dataList :+ data) match {
            case Some(mergeData) =>
              clientManager ! mergeData
            case None => Unit
          }
          context.become(processing(emptyList))
        case index if index < number =>
          context.become(processing(dataList :+ data))
        case _ =>
          context.become(processing(emptyList))
      }
    case _ =>
      logger.warn("VehicleClassNamesDataListener received unknown message.")
  }
}
