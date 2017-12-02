package pcars2tb.udp.listener

import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.io.{ IO, Udp }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.typesafe.scalalogging.LazyLogging
import java.net.InetSocketAddress
import pcars2tb.UsingActor._
import pcars2tb.udp.factory.{
  LapTimeDetailsFactory,
  TimeAggregateFactory,
  FuelDataFactory
}
import pcars2tb.udp.listener.UdpDataReader.readUdpData
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.Exception.catching

class UdpListener(clientManager: ActorRef) extends Actor with LazyLogging {

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

  val lapTimeDetailsListener = context.actorOf(
    Props(classOf[LapTimeDetailsFactory], clientManager),
    "LapTimeDetailsFactory"
  )

  val fuelDataListener = context.actorOf(
    Props(classOf[FuelDataFactory], clientManager),
    "FuelDataFactory"
  )

  // val timeAggregateListener = context.actorOf(
  // Props(classOf[TimeAggregateFactory], clientManager),
  // "TimeAggregateFactory"
  // )

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
            lapTimeDetailsListener ! udpData
            fuelDataListener ! udpData
            clientManager ! udpData
          // timeAggregateListener ! udpData
          case udpData: RaceData =>
            lapTimeDetailsListener ! udpData
            fuelDataListener ! udpData
            clientManager ! udpData
          // timeAggregateListener ! udpData
          case udpData: ParticipantsData =>
            participantsDataListener ! udpData
          case udpData: TimingsData =>
            lapTimeDetailsListener ! udpData
            fuelDataListener ! udpData
            clientManager ! udpData
          // timeAggregateListener ! udpData
          case udpData: GameStateData =>
            lapTimeDetailsListener ! udpData
            fuelDataListener ! udpData
            clientManager ! udpData
          // timeAggregateListener ! udpData
          case udpData: TimeStatsData =>
            lapTimeDetailsListener ! udpData
            clientManager ! udpData
          // timeAggregateListener ! udpData
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
    import java.text.SimpleDateFormat
    import java.util.Calendar
    import pcars2tb.udp.listener.PacketSize
    import pcars2tb.udp.listener.UdpDataReader.readPacketBase
    import pcars2tb.udp.listener.UdpStreamerPacketHandlerType._
    import pcars2tb.util.FileUtil

    val c = Calendar.getInstance()
    val sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS")
    val time = sdf.format(c.getTime())
    val dir = s"${FileUtil.currentDirectory}/sample"

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
