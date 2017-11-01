package com.github.nabezokodaikon

import akka.actor.{ ActorSystem, Props }
import akka.pattern.{ AskTimeoutException, gracefulStop }
import akka.stream.ActorMaterializer
import com.github.nabezokodaikon.db.DBAccessor
import com.github.nabezokodaikon.util.FileUtil
import com.github.nabezokodaikon.util.Loan.using
import com.typesafe.config.{ ConfigFactory }
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.Await
import scala.concurrent.duration._

object ActorDone

object UsingActor {
  implicit val system = ActorSystem("pcars2-udp-app")
  implicit val materializer = ActorMaterializer()
}

object Main extends /* App with */ LazyLogging {
  import UsingActor._

  def boot(dac: DBAccessor): Unit = {
    val config = ConfigFactory.load

    val clientManagerProps = Props(classOf[ClientManager])
    val clientManager = system.actorOf(clientManagerProps, "clientManager")

    // val udpProps = Props(classOf[UdpListener], clientManager)
    // val udpListener = system.actorOf(udpProps, "udpListener")

    val server = new Server(clientManager, dac)

    // Test code.
    import com.github.nabezokodaikon.example.udp.UdpTestDataSender
    val udpSenderProps = Props(classOf[UdpTestDataSender], clientManager)
    val udpSender = system.actorOf(udpSenderProps, "udpSender")
    udpSender ! UdpTestDataSender.Received

    val ipAddress = config.getString("app.server.ip-address")
    val port = config.getInt("app.server.port")
    server.startServer(ipAddress, port, system)

    // Test code.
    try {
      val stopped = gracefulStop(udpSender, 5.seconds, ActorDone)
      Await.result(stopped, 6.seconds)
    } catch {
      case e: AskTimeoutException =>
        logger.error(e.getMessage)
    }

    // try {
    // val stopped = gracefulStop(udpListener, 5.seconds, ActorDone)
    // Await.result(stopped, 6.seconds)
    // } catch {
    // case e: AskTimeoutException =>
    // logger.error(e.getMessage)
    // }

    try {
      val stopped = gracefulStop(clientManager, 5.seconds, ActorDone)
      Await.result(stopped, 6.seconds)
    } catch {
      case e: AskTimeoutException =>
        logger.error(e.getMessage)
    }

    system.terminate()
  }

  val file: String = s"${FileUtil.currentDirectory}/app.db"
  using(new DBAccessor(file)) { dac =>
    boot(dac)
  }
}
