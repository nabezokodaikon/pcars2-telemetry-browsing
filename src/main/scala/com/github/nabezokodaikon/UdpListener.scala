package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.io.{ IO, Udp }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.github.nabezokodaikon.pcars2.{
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
  import UdpListener._

  override def preStart() = {
    logger.debug("UdpListener preStart.");

    context.actorOf(Props[TestActor], name = "myChild")
    context.actorOf(Props[TestActor2], name = "myChild2")

    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("0.0.0.0", 5606))
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

  def receive = {
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
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: TimingsData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: GameStateData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: TimeStatsData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: ParticipantVehicleNamesData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
          case udpData: VehicleClassNamesData =>
            clientManager ! OutgoingValue(udpData.toJsonString)
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

class TestActor extends Actor {
  override def preStart() = {
    println("TestActor preStart.");
  }

  override def postStop() = {
    println("TestActor postStop.");
  }

  def receive = {
    case m: String => Unit
  }
}

class TestActor2 extends Actor {
  override def preStart() = {
    println("TestActor2 preStart.");
  }

  override def postStop() = {
    println("TestActor2 postStop.");
  }

  def receive = {
    case m: String => Unit
  }
}
