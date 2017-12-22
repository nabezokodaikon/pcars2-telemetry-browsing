package pcars2tb.option

import akka.actor.Actor
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.LazyLogging
import pcars2tb.db.OptionMapDBAccessor
import pcars2tb.util.Loan.using
import spray.json.DefaultJsonProtocol

trait OptionJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val unitOptionFormat = jsonFormat2(UnitOption)
  implicit val allOptionsFormat = jsonFormat3(Options)
}

/*
 * key: unit/
 *        isCelsius
 *        isMeter
 *        isBar
 */
final case class UnitOption(unit: String, value: Boolean)

final case class Options(
    isCelsius: UnitOption,
    isMeter: UnitOption,
    isBar: UnitOption
)

final object OptionAccessor extends LazyLogging {

  private def toUnitKey(unit: String) = s"unit/${unit}"

  private val defaultUnitValue = true

  def getAllOptions(): Options =
    using(new OptionMapDBAccessor()) { dac =>
      val isCelsius = dac.unitMap.getOrDefault(toUnitKey("isCelsius"), defaultUnitValue)
      val isMeter = dac.unitMap.getOrDefault(toUnitKey("isMeter"), defaultUnitValue)
      val isBar = dac.unitMap.getOrDefault(toUnitKey("isBar"), defaultUnitValue)
      Options(
        isCelsius = UnitOption("isCelsius", isCelsius),
        isMeter = UnitOption("isMeter", isMeter),
        isBar = UnitOption("isBar", isBar)
      )
    }

  def updateUnit(unit: String, value: Boolean): UnitOption =
    using(new OptionMapDBAccessor()) { dac =>
      val key = toUnitKey(unit)
      dac.unitMap.put(key, value)
      UnitOption(unit, value)
    }
}

final class OptionAccessor extends Actor with LazyLogging {
  import OptionAccessor._

  override def preStart() = {
    logger.debug("OptionAccessor preStart.");
  }

  override def postStop() = {
    logger.debug("OptionAccessor postStop.");
  }

  def receive(): Receive = {
    case "all" => sender ! getAllOptions()
    case UnitOption(unit, value) => sender ! updateUnit(unit, value)
    case _ =>
      logger.warn("OptionAccessor received unknown message.")
  }
}
