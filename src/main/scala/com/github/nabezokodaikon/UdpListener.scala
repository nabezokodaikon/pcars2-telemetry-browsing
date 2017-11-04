package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.io.{ IO, Udp }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.github.nabezokodaikon.pcars2.{
  UdpDataMerger,
  TelemetryData,
  RaceData,
  ParticipantsData,
  TimingsData,
  GameStateData,
  TimeStatsData,
  ParticipantVehicleNamesData,
  VehicleClassNamesData
}
import com.github.nabezokodaikon.pcars2.UdpDataReader.readUdpData
import com.typesafe.scalalogging.LazyLogging
import java.net.InetSocketAddress
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.Exception.catching

object UdpListener {
  case class OutgoingValue(value: String)
}

class UdpListener(clientManager: ActorRef) extends Actor with LazyLogging {
  import UsingActor._
  import UdpListener.OutgoingValue

  val participantsDataListener = context.actorOf(
    Props(classOf[ParticipantsDataListener], clientManager),
    "ParticipantsDataListener"
  )

  val participantVehicleNamesDataListener = context.actorOf(
    Props(classOf[ParticipantVehicleNamesDataListener], clientManager),
    "ParticipantVehicleNamesDataListener"
  )

  val vehicleClassNamesDataListener = context.actorOf(
    Props(classOf[VehicleClassNamesDataListener], clientManager),
    "VehicleClassNamesDataListener"
  )

  override def preStart() = {
    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("0.0.0.0", 5606))

    logger.debug("UdpListener preStart.");
  }

  override def postStop() = {
    context.children.foreach { child =>
      catching(classOf[AskTimeoutException]).either {
        val stopped = gracefulStop(child, 5.seconds, PoisonPill)
        Await.result(stopped, 6.seconds)
      } match {
        case Left(e) => logger.error(e.getMessage)
        case _ => Unit
      }
    }

    logger.debug("UdpListener postStop.");
  }

  def receive(): Receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
    case _ =>
      logger.warn("UdpListener received unknown message.")
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      readUdpData(data.toArray) match {
        case Some(d) => d match {
          case udpData: TelemetryData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: RaceData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: ParticipantsData =>
            participantsDataListener ! udpData
          case udpData: TimingsData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: GameStateData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: TimeStatsData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: ParticipantVehicleNamesData =>
            participantVehicleNamesDataListener ! udpData
          case udpData: VehicleClassNamesData =>
            vehicleClassNamesDataListener ! udpData
        }
        case None => Unit
      }
    // createTestData(data.toArray)
    case Udp.Unbind =>
      logger.debug("UDP unbind.")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      logger.debug("UDP unbound.")
      context.stop(self)
    case _ =>
      logger.warn("UdpListener received unknown message.")
  }

  /*
   * Create test data method.
   */
  def createTestData(data: Array[Byte]) = {
    import com.github.nabezokodaikon.pcars2.PacketSize
    import com.github.nabezokodaikon.pcars2.UdpDataReader.readPacketBase
    import com.github.nabezokodaikon.pcars2.UdpStreamerPacketHandlerType._
    import com.github.nabezokodaikon.util.FileUtil
    import java.text.SimpleDateFormat
    import java.util.Calendar

    val c = Calendar.getInstance()
    val sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS")
    val time = sdf.format(c.getTime())
    val dir = FileUtil.currentDirectory

    val (p, _) = readPacketBase(data.toList)
    p.packetType match {
      case CAR_PHYSICS =>
        val name = s"${dir}/testdata/pcars2/${CAR_PHYSICS}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case RACE_DEFINITION =>
        val name = s"${dir}/testdata/pcars2/${RACE_DEFINITION}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case PARTICIPANTS =>
        val name = s"${dir}/testdata/pcars2/${PARTICIPANTS}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case TIMINGS =>
        val name = s"${dir}/testdata/pcars2/${TIMINGS}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case GAME_STATE =>
        val name = s"${dir}/testdata/pcars2/${GAME_STATE}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case WEATHER_STATE =>
        val name = s"${dir}/testdata/pcars2/${WEATHER_STATE}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case VEHICLE_NAMES =>
        val name = s"${dir}/testdata/pcars2/${VEHICLE_NAMES}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case TIME_STATS =>
        val name = s"${dir}/testdata/pcars2/${TIME_STATS}_${time}.bin"
        FileUtil.writeBinary(name, data)
      case PARTICIPANT_VEHICLE_NAMES => data.length match {
        case PacketSize.PARTICIPANT_VEHICLE_NAMES_DATA =>
          val name = s"${dir}/testdata/pcars2/${PARTICIPANT_VEHICLE_NAMES}_p_${time}.bin"
          FileUtil.writeBinary(name, data)
        case PacketSize.VEHICLE_CLASS_NAMES_DATA =>
          val name = s"${dir}/testdata/pcars2/${PARTICIPANT_VEHICLE_NAMES}_v_${time}.bin"
          FileUtil.writeBinary(name, data)
      }
      case _ =>
        logger.warn(s"Unknown packet type: ${p.packetType}")
    }
  }
}

trait DataListener[T] extends Actor with LazyLogging {
  import Actor.Receive
  val emptyList: List[T] = List[T]()
  def receive(): Receive = processing(List[T]())
  def processing(dataList: List[T]): Receive
}

final class ParticipantsDataListener(clientManager: ActorRef)
  extends DataListener[ParticipantsData] {
  import UdpListener.OutgoingValue

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
          val mergeData = UdpDataMerger.merge(dataList :+ data)
          clientManager ! OutgoingValue(mergeData.toJsonString)
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
  extends DataListener[ParticipantVehicleNamesData] {
  import UdpListener.OutgoingValue

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
          val mergeData = UdpDataMerger.merge(dataList :+ data)
          clientManager ! OutgoingValue(mergeData.toJsonString)
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
  extends DataListener[VehicleClassNamesData] {
  import UdpListener.OutgoingValue

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
          val mergeData = UdpDataMerger.merge(dataList :+ data)
          clientManager ! OutgoingValue(mergeData.toJsonString)
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
