package com.github.nabezokodaikon

import akka.actor.{ ActorRef, Props }
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.NotUsed
import akka.http.scaladsl.server.directives._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

class Server(manager: ActorRef) extends LazyLogging {

  private val resourceDirectory = {
    val current = FileUtil.getCurrentDirectory
    s"${current}/resource"
  }

  private def createUser() = {
    import UsingActor._

    // val props = Props(classOf[Client], manager)
    // val clientActor = system.actorOf(props)

    val sink = Sink.ignore

    // val source: Source[TextMessage, NotUsed] =
    // Source.actorRef[UdpListener.OutgoingValue](10, OverflowStrategy.fail)
    // .mapMaterializedValue { outActor =>
    // println(outActor.toString)
    // clientActor ! Client.Connected(outActor)
    // NotUsed
    // }
    // .map { outValue: UdpListener.OutgoingValue =>
    // TextMessage(outValue.value)
    // }
    val source = Source.fromGraph(new ClientStage(manager))
      .map((value: UdpListener.OutgoingValue) => TextMessage(value.value))

    Flow.fromSinkAndSource(sink, source)
    //
    // val flow = Flow.fromGraph(new ClientStage(manager))
    // flow
  }

  val route: Route =
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
