package com.github.nabezokodaikon.util

import java.math.{ BigDecimal, RoundingMode }

object NumericUtil {
  import scala.language.implicitConversions

  private def calcDivide(value: BigDecimal, divisor: BigDecimal, scale: Int): String =
    if (scale > -1)
      value.divide(divisor, scale, RoundingMode.DOWN).toString
    else
      value.divide(divisor, 0, RoundingMode.DOWN).toString

  private def calcMultiply(value: BigDecimal, multiplicand: BigDecimal, scale: Int): String =
    if (scale > -1)
      value.multiply(multiplicand).setScale(scale, RoundingMode.DOWN).toString
    else
      value.multiply(multiplicand).setScale(0, RoundingMode.DOWN).toString

  trait RoundingSupport {
    val value: BigDecimal

    def divide(divisor: Byte, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def divide(divisor: Short, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def divide(divisor: Int, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def divide(divisor: Long, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def divide(divisor: Float, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def divide(divisor: Double, scale: Int): String =
      calcDivide(value, new BigDecimal(divisor), scale)

    def multiply(multiplicand: Float, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)

    def multiply(multiplicand: Double, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)
  }

  class ByteRoundingSupport(srcValue: Byte) extends RoundingSupport {
    val value = new BigDecimal(srcValue)
  }

  class ShortRoundingSupport(srcValue: Short) extends RoundingSupport {
    val value = new BigDecimal(srcValue)
  }

  class IntRoundingSupport(srcValue: Int) extends RoundingSupport {
    val value = new BigDecimal(srcValue)
  }

  class LongRoundingSupport(srcValue: Long) extends RoundingSupport {
    val value = new BigDecimal(srcValue)
  }

  class FloatRoundingSupport(srcValue: Float) extends RoundingSupport {
    val value = new BigDecimal(srcValue)

    def multiply(multiplicand: Byte, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)

    def multiply(multiplicand: Short, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)

    def multiply(multiplicand: Int, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)

    def multiply(multiplicand: Long, scale: Int): String =
      calcMultiply(value, new BigDecimal(multiplicand), scale)
  }

  implicit def toBigDecimal(value: Byte) = new ByteRoundingSupport(value)
  implicit def toBigDecimal(value: Short) = new ShortRoundingSupport(value)
  implicit def toBigDecimal(value: Int) = new IntRoundingSupport(value)
  implicit def toBigDecimal(value: Long) = new LongRoundingSupport(value)
  implicit def toBigDecimal(value: Float) = new FloatRoundingSupport(value)
}
