package pcars2tb.option

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait OptionJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val unitOptionFormat = jsonFormat2(UnitOption)
  implicit val allOptionsFormat = jsonFormat3(AllOptions)
}

/*
 * key: unit/
 *        isCelsius
 *        isMeter
 *        isBar
 */
final case class UnitOption(key: String, value: Boolean)

final case class AllOptions(
    isCelsius: UnitOption,
    isMeter: UnitOption,
    isBar: UnitOption
)
