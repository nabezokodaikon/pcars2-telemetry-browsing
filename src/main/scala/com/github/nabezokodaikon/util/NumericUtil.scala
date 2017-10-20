package com.github.nabezokodaikon.util

import java.math.{ BigDecimal, RoundingMode }

object NumericUtil {
  import scala.language.implicitConversions

  private def calc(valueDecimal: BigDecimal, divisionValueDecimal: BigDecimal, scale: Int): String =
    scale match {
      case a if a > -1 =>
        valueDecimal.divide(divisionValueDecimal, scale, RoundingMode.DOWN).toString
      case _ =>
        valueDecimal.divide(divisionValueDecimal, 0, RoundingMode.DOWN).toString
    }

  class IntRoundingSupport(value: Int) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calc(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calc(valueDecimal, divisionValueDecimal, scale)
    }
  }

  class LongRoundingSupport(value: Long) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calc(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Long, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calc(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calc(valueDecimal, divisionValueDecimal, scale)
    }
  }

  class FloatRoundingSupport(value: Float) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value.toString)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calc(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value.toString)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calc(valueDecimal, divisionValueDecimal, scale)
    }
  }

  implicit def toBigDecimal(value: Int) = new IntRoundingSupport(value)
  implicit def toBigDecimal(value: Long) = new LongRoundingSupport(value)
  implicit def toBigDecimal(value: Float) = new FloatRoundingSupport(value)
}
