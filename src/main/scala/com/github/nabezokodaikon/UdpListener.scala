package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import akka.io.{ IO, Udp }
import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory.getJsonText
import com.typesafe.scalalogging.LazyLogging
import java.net.InetSocketAddress

object UdpListener {
  case class OutgoingValue(value: String)
}

class UdpListener(clientManager: ActorRef) extends Actor with LazyLogging {
  import UsingActor._
  import UdpListener._

  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("0.0.0.0", 5606))

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
    case _ =>
      logger.warn("UdpListener received unknown message.")
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      val dataArray = data.toArray
      clientManager ! OutgoingValue(getJsonText(dataArray))
    // output(dataArray)
    // confirm(data.toList)
    case Udp.Unbind =>
      logger.debug("UDP unbind.")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      logger.debug("UDP unbound.")
      context.stop(self)
    case ActorDone =>
      println("UdpListener Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  def confirm(data: List[Byte]) = {
    import com.github.nabezokodaikon.pcars1.TelemetryDataStructFactory._
    import com.github.nabezokodaikon.pcars1.TelemetryDataConst._
    val frameInfo = createFrameInfo(data)
    if (frameInfo.frameType == TELEMETRY_DATA_FRAME_TYPE) {
      val telemetryData = createTelemetryData(data)
      // println(telemetryData.carStateData.brake)
      // println(telemetryData.carStateData.throttle.toString)
      // println(telemetryData.carStateData.clutch)
      // println(telemetryData.carStateData.steering)
      // println(telemetryData.carStateData.speed)
      // println(telemetryData.carStateData.gear)
      // println(telemetryData.carStateData.numGears)
      // println(telemetryData.carStateData.rpm)
    }
  }

  def output(data: Array[Byte]) = {
    import com.github.nabezokodaikon.util.FileUtil
    import java.util.Calendar
    import java.text.SimpleDateFormat
    import com.github.nabezokodaikon.pcars1._

    val c = Calendar.getInstance()
    val sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS")
    val time = sdf.format(c.getTime())
    val dir = FileUtil.currentDirectory
    val name = s"${dir}/testdata/${time}.bin"

    val info = TelemetryDataStructFactory.createFrameInfo(data.toList)
    info.frameType match {
      case TelemetryDataConst.TELEMETRY_DATA_FRAME_TYPE =>
        val name = s"${dir}/testdata/0_${time}.bin"
        FileUtil.writeBinary(name, data)
      case TelemetryDataConst.PARTICIPANT_INFO_STRINGS_FRAME_TYPE =>
        val name = s"${dir}/testdata/1_${time}.bin"
        FileUtil.writeBinary(name, data)
      case TelemetryDataConst.PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE =>
        val name = s"${dir}/testdata/2_${time}.bin"
        FileUtil.writeBinary(name, data)
    }
  }
}
