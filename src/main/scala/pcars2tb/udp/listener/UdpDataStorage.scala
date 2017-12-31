package pcars2tb.udp.listener

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging

final case class Storage(
    raceData: Option[(RaceData, String)],
    participantsData: Option[(ParticipantsData, String)],
    participantVehicleNamesData: Option[(ParticipantVehicleNamesData, String)],
    vehicleClassNamesData: Option[(VehicleClassNamesData, String)],
    lapTimeDetails: Option[(LapTimeDetails, String)],
    realTimeGap: Option[(RealTimeGap, String)],
    aggregateTime: Option[(AggregateTime, String)],
    fuelData: Option[(FuelData, String)],
    telemetrySummary: Option[(TelemetrySummary, String)]
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
    realTimeGap = None,
    aggregateTime = None,
    fuelData = None,
    telemetrySummary = None
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
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: ParticipantsData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = Some((udpData, json)),
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: ParticipantVehicleNamesData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = Some((udpData, json)),
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: VehicleClassNamesData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = Some((udpData, json)),
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: LapTimeDetails =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = Some((udpData, json)),
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: RealTimeGap =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = Some((udpData, json)),
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: AggregateTime =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = Some((udpData, json)),
          fuelData = storage.fuelData,
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: FuelData =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = Some((udpData, json)),
          telemetrySummary = storage.telemetrySummary
        )))
      case udpData: TelemetrySummary =>
        context.become(processing(Storage(
          raceData = storage.raceData,
          participantsData = storage.participantsData,
          participantVehicleNamesData = storage.participantVehicleNamesData,
          vehicleClassNamesData = storage.vehicleClassNamesData,
          lapTimeDetails = storage.lapTimeDetails,
          realTimeGap = storage.realTimeGap,
          aggregateTime = storage.aggregateTime,
          fuelData = storage.fuelData,
          telemetrySummary = Some((udpData, json))
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
      for ((_, json) <- storage.realTimeGap) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.aggregateTime) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.fuelData) client ! json
      Thread.sleep(10)
      for ((_, json) <- storage.telemetrySummary) client ! json
    case _ =>
      logger.warn(s"UdpDataStorage received unknown message.")
  }
}
