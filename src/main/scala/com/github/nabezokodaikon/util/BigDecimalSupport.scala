package com.github.nabezokodaikon.util

import java.math.{ BigDecimal, RoundingMode }
import scala.language.implicitConversions

object BigDecimalSupport {

  trait BigDecimalSupport {
    val value: BigDecimal
  }

  trait TimeToStringSupport extends BigDecimalSupport {

    val divisor60 = new BigDecimal(60)
    val divisor1000 = new BigDecimal(1000)
    val minusValue = new BigDecimal(-1)

    def toHourFormatFromMilliseconds(): String = {
      value.compareTo(BigDecimal.ZERO) match {
        case -1 => "--:--:--.---"
        case _ =>
          value.divideAndRemainder(divisor1000) match {
            case Array(srcSeconds, milliseconds) =>
              srcSeconds.divideAndRemainder(divisor60) match {
                case Array(srcMinutes, seconds) =>
                  srcMinutes.divideAndRemainder(divisor60) match {
                    case Array(hours, minutes) =>
                      s"${"%02d".format(hours.intValue)}:${"%02d".format(minutes.intValue)}:${"%02d".format(seconds.intValue)}.${"%03d".format(milliseconds.intValue)}"
                  }
              }
          }
      }
    }

    def toHourFormatFromSeconds(): String = {
      value.compareTo(BigDecimal.ZERO) match {
        case -1 => "--:--:--.---"
        case _ =>
          value.divideAndRemainder(divisor60) match {
            case Array(srcMinutes, seconds) =>
              srcMinutes.divideAndRemainder(divisor60) match {
                case Array(hours, minutes) =>
                  val milliseconds = seconds.multiply(divisor1000).remainder(divisor1000)
                  s"${"%02d".format(hours.intValue)}:${"%02d".format(minutes.intValue)}:${"%02d".format(seconds.intValue)}.${"%03d".format(milliseconds.intValue)}"
              }
          }
      }
    }

    def toMinuteFormatFromMilliseconds(): String = {
      value.compareTo(BigDecimal.ZERO) match {
        case -1 => "--:--.---"
        case _ =>
          value.divideAndRemainder(divisor1000) match {
            case Array(srcSeconds, milliseconds) =>
              srcSeconds.divideAndRemainder(divisor60) match {
                case Array(minutes, seconds) =>
                  s"${"%02d".format(minutes.intValue)}:${"%02d".format(seconds.intValue)}.${"%03d".format(milliseconds.intValue)}"
              }
          }
      }
    }

    def toMinuteFormatFromSeconds(): String = {
      value.compareTo(BigDecimal.ZERO) match {
        case -1 => "--:--.---"
        case _ =>
          value.divideAndRemainder(divisor60) match {
            case Array(minutes, seconds) =>
              val milliseconds = seconds.multiply(divisor1000).remainder(divisor1000)
              s"${"%02d".format(minutes.intValue)}:${"%02d".format(seconds.intValue)}.${"%03d".format(milliseconds.intValue)}"
          }
      }
    }
  }

  trait RoundingSupport extends BigDecimalSupport {

    def calcDivide(value: BigDecimal, divisor: BigDecimal, scale: Int): String =
      if (scale > -1)
        value.divide(divisor, scale, RoundingMode.HALF_UP).toString
      else
        value.divide(divisor, 0, RoundingMode.HALF_UP).toString

    def calcMultiply(value: BigDecimal, multiplicand: BigDecimal, scale: Int): String =
      if (scale > -1)
        value.multiply(multiplicand).setScale(scale, RoundingMode.HALF_UP).toString
      else
        value.multiply(multiplicand).setScale(0, RoundingMode.HALF_UP).toString

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

    def toRound(scale: Int): String =
      if (scale > -1)
        value.setScale(scale, RoundingMode.DOWN).toString
      else
        value.setScale(0, RoundingMode.DOWN).toString
  }

  class ByteSupport(srcValue: Byte)
    extends RoundingSupport
    with TimeToStringSupport {

    val value = new BigDecimal(srcValue)
  }

  class ShortSupport(srcValue: Short)
    extends RoundingSupport
    with TimeToStringSupport {

    val value = new BigDecimal(srcValue)
  }

  class IntSupport(srcValue: Int)
    extends RoundingSupport
    with TimeToStringSupport {

    val value = new BigDecimal(srcValue)
  }

  class LongSupport(srcValue: Long)
    extends RoundingSupport
    with TimeToStringSupport {

    val value = new BigDecimal(srcValue)
  }

  class FloatSupport(srcValue: Float)
    extends RoundingSupport
    with TimeToStringSupport {

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

  implicit def toBigDecimal(value: Byte) = new ByteSupport(value)
  implicit def toBigDecimal(value: Short) = new ShortSupport(value)
  implicit def toBigDecimal(value: Int) = new IntSupport(value)
  implicit def toBigDecimal(value: Long) = new LongSupport(value)
  implicit def toBigDecimal(value: Float) = new FloatSupport(value)
}
