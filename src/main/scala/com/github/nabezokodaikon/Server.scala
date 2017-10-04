package com.github.nabezokodaikon

import akka.actor.{ ActorRef, Props }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.http.scaladsl.server.directives._
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging
import spray.json.DefaultJsonProtocol

// final case class User(userName: String, userAge: Int)
// final case class Group(groupName: String)
// final case class Info(user: User, group: Group)
// trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  // implicit val userFormat = jsonFormat2(User)
  // implicit val groupFormat = jsonFormat1(Group)
  // implicit val infoFormat = jsonFormat2(Info)
// }

class Server(manager: ActorRef) extends HttpApp with JsonSupport with LazyLogging {

  private val resourceDirectory = {
    val current = FileUtil.getCurrentDirectory
    s"${current}/resource"
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
        val file = s"${resourceDirectory}/index.html"
        val contentType = FileUtil.getContentType(file)
        val text = FileUtil.readText(file)
        complete(HttpEntity(contentType, text))
      }
    } ~
      path("api") {
        get {
          handleWebSocketMessages(createUser())
        }
      } ~
      path(Segments) { x: List[String] =>
        get {
          val segments = x.mkString("/")
          val file = s"${resourceDirectory}/${segments}"
          val contentType = FileUtil.getContentType(file)
          val text = FileUtil.readText(file)
          complete(HttpEntity(contentType, text))
        }
      }
}
