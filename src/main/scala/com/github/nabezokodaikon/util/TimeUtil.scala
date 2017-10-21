package com.github.nabezokodaikon.util

import java.math.{ BigDecimal, RoundingMode }
import scala.language.implicitConversions

object TimeUtil {

  class TimeToStringSupport(srcValue: Float) {
    val value = new BigDecimal(srcValue.toString)
  }

  implicit def toTimeString(value: Float) = new TimeToStringSupport(value)
}

