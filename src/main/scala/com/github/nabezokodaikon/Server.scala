package com.github.nabezokodaikon

import akka.actor.{ ActorRef, Props }
import akka.http.scaladsl.model.{ HttpEntity, StatusCodes }
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.http.scaladsl.server.directives._
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.github.nabezokodaikon.util.FileUtil
import com.github.nabezokodaikon.db.DBEntityJsonProtocol
import com.github.nabezokodaikon.db.UnitOption
import com.typesafe.scalalogging.LazyLogging

class Server(manager: ActorRef) extends HttpApp with LazyLogging with DBEntityJsonProtocol {

  private val contentsDirectory = {
    val current = FileUtil.currentDirectory
    s"${current}/public"
  }

  private val contentsDistDirectory = {
    val current = FileUtil.currentDirectory
    s"${current}/public/dist"
  }

  private def createUser() = {
    val sink = Sink.ignore
    val source = Source.fromGraph(new ClientStage(manager))
      .map((value: UdpListener.OutgoingValue) => TextMessage(value.value))
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
      path("debug") {
        get {
          val file = s"${contentsDirectory}/debug.html"
          val contentType = FileUtil.getContentType(file)
          val text = FileUtil.readBinary(file)
          complete(HttpEntity(contentType, text))
        }
      } ~
      path("pcars1") {
        get {
          handleWebSocketMessages(createUser())
        }
      } ~
      pathPrefix("options") {
        path("unit") {
          post {
            entity(as[UnitOption]) { req =>
              // TODO Update database
              complete(req)
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
