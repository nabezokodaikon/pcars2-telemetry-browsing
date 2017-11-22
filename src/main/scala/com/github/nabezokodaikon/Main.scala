package com.github.nabezokodaikon

import akka.actor.{ PoisonPill, Props }
import akka.io.Udp
import akka.pattern.{ AskTimeoutException, gracefulStop }
import com.github.nabezokodaikon.UsingActor._
import com.github.nabezokodaikon.db.OptionDBAccessor
import com.github.nabezokodaikon.udpListener.UdpListener
import com.github.nabezokodaikon.util.FileUtil
import com.github.nabezokodaikon.util.Loan.using
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.Exception.catching

object Main extends App with LazyLogging {

  def debug(dac: OptionDBAccessor): Unit = {
    val clientManagerProps = Props(classOf[ClientManager])
    val clientManager = system.actorOf(clientManagerProps, "clientManager")
    val server = new Server(clientManager, dac)

    import com.github.nabezokodaikon.example.udp.UdpTestDataSender
    val udpTestDataSenderProps = Props(classOf[UdpTestDataSender], clientManager)
    val udpTestDataSender = system.actorOf(udpTestDataSenderProps, "udpSender")
    udpTestDataSender ! UdpTestDataSender.Received

    val ipAddress = config.getString("app.server.ip-address")
    val port = config.getInt("app.server.port")
    println(s"Please access 'http://${ipAddress}:${port}' on the Web browser.")
    server.startServer(ipAddress, port, system)

    catching(classOf[AskTimeoutException]).either {
      val stopped = gracefulStop(udpTestDataSender, 5.seconds, PoisonPill)
      Await.result(stopped, 6.seconds)
    } match {
      case Left(e) => logger.error(e.getMessage)
      case _ => Unit
    }

    catching(classOf[AskTimeoutException]).either {
      val stopped = gracefulStop(clientManager, 5.seconds, PoisonPill)
      Await.result(stopped, 6.seconds)
    } match {
      case Left(e) => logger.error(e.getMessage)
      case _ => Unit
    }

    system.terminate()
  }

  def boot(dac: OptionDBAccessor): Unit = {
    val clientManagerProps = Props(classOf[ClientManager])
    val clientManager = system.actorOf(clientManagerProps, "clientManager")

    val udpProps = Props(classOf[UdpListener], clientManager)
    val udpListener = system.actorOf(udpProps, "udpListener")

    val server = new Server(clientManager, dac)

    val ipAddress = config.getString("app.server.ip-address")
    val port = config.getInt("app.server.port")
    println(s"Please access 'http://${ipAddress}:${port}' on the Web browser.")
    server.startServer(ipAddress, port, system)

    catching(classOf[AskTimeoutException]).either {
      udpListener ! Udp.Unbind
      val stopped = gracefulStop(udpListener, 5.seconds, PoisonPill)
      Await.result(stopped, 6.seconds)
    } match {
      case Left(e) => logger.error(e.getMessage)
      case _ => Unit
    }

    catching(classOf[AskTimeoutException]).either {
      val stopped = gracefulStop(clientManager, 5.seconds, PoisonPill)
      Await.result(stopped, 6.seconds)
    } match {
      case Left(e) => logger.error(e.getMessage)
      case _ => Unit
    }

    system.terminate()
  }

  logger.debug("Application start")

  val config = ConfigFactory.load()
  val debug = config.getString("app.debug").toBoolean
  val file: String = s"${FileUtil.currentDirectory}/option.db"
  using(new OptionDBAccessor(file)) { dac =>
    if (!debug) {
      boot(dac)
    } else {
      debug(dac)
    }
  }

  logger.debug("Application termination")
}
