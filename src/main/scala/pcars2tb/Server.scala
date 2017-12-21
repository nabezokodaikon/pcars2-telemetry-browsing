package pcars2tb

import akka.actor.{ ActorRef, Props }
import akka.http.scaladsl.model.{
  HttpEntity,
  HttpResponse,
  StatusCodes
}
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.http.scaladsl.server.directives._
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.typesafe.config.{ Config, ConfigFactory }
import com.typesafe.scalalogging.LazyLogging
import pcars2tb.buttonbox.{
  ButtonBox,
  ButtonIndex,
  ButtonChar,
  ButtonLabel,
  ButtonBoxJsonProtocol
}
import pcars2tb.config.{
  ConfigEntityJsonProtocol,
  ConnectionInfo
}
import pcars2tb.db.{
  OptionMapDBAccessor
}
import pcars2tb.option.{
  AllOptions,
  OptionJsonProtocol,
  UnitOption
}
import pcars2tb.util.FileUtil
import pcars2tb.util.Loan.using

class Server(manager: ActorRef)
  extends HttpApp
  with LazyLogging
  with ConfigEntityJsonProtocol
  with OptionJsonProtocol
  with ButtonBoxJsonProtocol {

  private val contentsDirectory = {
    val current = FileUtil.currentDirectory
    s"${current}/public"
  }

  private val contentsDistDirectory = {
    val current = FileUtil.currentDirectory
    s"${current}/public/dist"
  }

  private def createClient() = {
    val sink = Sink.ignore
    val source = Source.fromGraph(new ClientStage(manager))
      .map(json => TextMessage(json))
    Flow.fromSinkAndSource(sink, source)
  }

  override def routes: Route =
    pathSingleSlash {
      get {
        val file = s"${contentsDirectory}/index.html"
        val contentType = FileUtil.getContentType(file)
        val text = FileUtil.readBinary(file)
        complete(HttpEntity(contentType, text))
      }
    } ~
      path("develop") {
        get {
          val file = s"${contentsDirectory}/indexDevelop.html"
          val contentType = FileUtil.getContentType(file)
          val text = FileUtil.readBinary(file)
          complete(HttpEntity(contentType, text))
        }
      } ~
      path("udp-data") {
        get {
          handleWebSocketMessages(createClient())
        }
      } ~
      pathPrefix("config") {
        path("connection-info") {
          get {
            val config = ConfigFactory.load
            val ipAddress = config.getString("app.server.ip-address")
            val port = config.getInt("app.server.port")
            complete(ConnectionInfo(ipAddress, port))
          }
        }
      } ~
      pathPrefix("buttonBox") {
        path("all") {
          get {
            val res = ButtonBox.getAllMappings()
            complete(res)
          }
        } ~
          path("action") {
            post {
              entity(as[ButtonIndex]) { req =>
                logger.debug(s"callAction: ${req}")
                ButtonBox.callAction(req)
                complete(HttpResponse(StatusCodes.Accepted))
              }
            }
          } ~
          path("char") {
            post {
              entity(as[ButtonChar]) { req =>
                val res = ButtonBox.updateChar(req)
                complete(res)
              }
            }
          } ~
          path("label") {
            post {
              entity(as[ButtonLabel]) { req =>
                val res = ButtonBox.updateLabel(req)
                complete(res)
              }
            }
          }
      } ~
      pathPrefix("option") {
        path("all") {
          get {
            using(new OptionMapDBAccessor()) { dac =>
              val isCelsius = dac.unitMap.getOrDefault("unit/isCelsius", true)
              val isMeter = dac.unitMap.getOrDefault("unit/isMeter", true)
              val isBar = dac.unitMap.getOrDefault("unit/isBar", true)
              val res = AllOptions(
                isCelsius = UnitOption("unit/isCelsius", isCelsius),
                isMeter = UnitOption("unit/isMeter", isMeter),
                isBar = UnitOption("unit/isBar", isBar)
              )
              complete(res)
            }
          }
        } ~
          path("unit") {
            post {
              entity(as[UnitOption]) { req =>
                using(new OptionMapDBAccessor()) { dac =>
                  dac.unitMap.put(req.key, req.value)
                  complete(req)
                }
              }
            }
          }
      } ~
      path(Segments) { x: List[String] =>
        get {
          val segments = x.mkString("/")

          val file = s"${contentsDirectory}/${segments}" match {
            case f if FileUtil.exists(f) => f
            case _ => s"${contentsDistDirectory}/${segments}"
          }

          val contentType = FileUtil.getContentType(file)
          val data = FileUtil.readBinary(file)
          complete(HttpEntity(contentType, data))
        }
      }
}
