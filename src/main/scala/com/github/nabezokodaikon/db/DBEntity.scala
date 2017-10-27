package com.github.nabezokodaikon.db

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

trait DBEntityJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val unitOptionFormat = jsonFormat2(UnitOption)
}

/*
 * key: options/
 *        isCelsius
 *        isMeter
 *        isBar
 */
final case class UnitOption(key: String, value: Boolean)
