package com.github.nabezokodaikon

import akka.actor.{ Actor, ActorRef }
import akka.io.{ IO, Udp }
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
      clientManager ! OutgoingValue(s"$data.toString")
    // output(data.toArray)
    case Udp.Unbind =>
      logger.debug("unbind")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      logger.debug("unbound")
      context.stop(self)
    case ActorDone =>
      println("UdpListener Done.")
      context.stop(self)
    case _ =>
      logger.warn("Received unknown message.")
  }

  def output(data: Array[Byte]) = {
    import com.github.nabezokodaikon.util.FileUtil
    import java.util.Calendar
    import java.text.SimpleDateFormat
    val c = Calendar.getInstance()
    val sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS")
    val time = sdf.format(c.getTime())
    val dir = FileUtil.getCurrentDirectory()
    val name = s"${dir}/testdata/${time}.bin"
    FileUtil.writeBinary(name, data)
  }
}
