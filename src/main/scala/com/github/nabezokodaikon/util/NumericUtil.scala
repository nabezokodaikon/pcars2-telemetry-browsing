package com.github.nabezokodaikon.util

import java.math.{ BigDecimal, RoundingMode }

object NumericUtil {
  import scala.language.implicitConversions

  private def calcDivide(a: BigDecimal, b: BigDecimal, scale: Int): String =
    if (scale > -1) a.divide(b, scale, RoundingMode.DOWN).toString
    else a.divide(b, 0, RoundingMode.DOWN).toString

  private def calcMultiply(a: BigDecimal, b: BigDecimal, scale: Int): String =
    if (scale > -1) a.multiply(b).setScale(scale, RoundingMode.DOWN).toString
    else a.multiply(b).setScale(0, RoundingMode.DOWN).toString

  class IntRoundingSupport(value: Int) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }
  }

  class LongRoundingSupport(value: Long) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Long, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }
  }

  class FloatRoundingSupport(value: Float) {
    def divide(divisionValue: Int, scale: Int): String = {
      val valueDecimal = new BigDecimal(value.toString)
      val divisionValueDecimal = new BigDecimal(divisionValue)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }

    def divide(divisionValue: Float, scale: Int): String = {
      val valueDecimal = new BigDecimal(value.toString)
      val divisionValueDecimal = new BigDecimal(divisionValue.toString)
      calcDivide(valueDecimal, divisionValueDecimal, scale)
    }
  }

  implicit def toBigDecimal(value: Int) = new IntRoundingSupport(value)
  implicit def toBigDecimal(value: Long) = new LongRoundingSupport(value)
  implicit def toBigDecimal(value: Float) = new FloatRoundingSupport(value)
}
