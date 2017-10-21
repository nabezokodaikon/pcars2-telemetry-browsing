package com.github.nabezokodaikon.util

import java.math.BigDecimal
import scala.language.implicitConversions

object TimeUtil {

  val divisor60 = new BigDecimal(60)
  val divisor1000 = new BigDecimal(1000)
  val minusValue = new BigDecimal(-1)

  class TimeToStringSupport(floatSeconds: Float) {
    val srcSeconds = new BigDecimal(floatSeconds.toString)

    def toTimeFormatFromSeconds(): String = {
      srcSeconds.divideAndRemainder(divisor60) match {
        case Array(srcMinutes, seconds) if srcMinutes.compareTo(minusValue) > 0 =>
          srcMinutes.divideAndRemainder(divisor60) match {
            case Array(hours, minutes) if hours.compareTo(minusValue) > 0 =>
              val milliseconds = seconds.multiply(divisor1000).remainder(divisor1000)
              s"${"%02d".format(hours.intValue)}:${"%02d".format(minutes.intValue)}:${"%02d".format(seconds.intValue)}.${"%03d".format(milliseconds.intValue)}"
            case _ => "--:--:--.---"
          }
        case _ => "--:--:--.---"
      }
    }
  }

  implicit def toBigDecimal(value: Float) = new TimeToStringSupport(value)
}

