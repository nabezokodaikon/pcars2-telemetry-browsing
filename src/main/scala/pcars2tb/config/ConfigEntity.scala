package pcars2tb.config

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

trait ConfigEntityJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val connectionInfoFormat = jsonFormat2(ConnectionInfo)
}

final case class ConnectionInfo(ipAddress: String, port: Int)
