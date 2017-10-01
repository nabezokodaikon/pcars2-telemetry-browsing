package com.github.nabezokodaikon

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
// import akka.NotUsed
// import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object HttpServer extends LazyLogging {

  private val resourceDirectory = {
    val current = FileUtil.getCurrentDirectory
    s"${current}/resource"
  }

  // def httpResource(file: String) {
  // val source = Source.single(file)
  // val flow = Flow[String]
  // .map { f =>
  // val contentType = FileUtil.getContentType(f)
  // val text = FileUtil.readText(f)
  // HttpEntity(contentType, text)
  // }
  // val sink = Sink.last[]()
  // println(sink.toString)
  // }

  val route: Route =
    pathSingleSlash {
      get {
        val file = s"${resourceDirectory}/index.html"
        val contentType = FileUtil.getContentType(file)
        val text = FileUtil.readText(file)
        complete(HttpEntity(contentType, text))
      }
    } ~
      // TODO
      // path("api") {
      // post {
      // }
      // }
      // } ~
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
