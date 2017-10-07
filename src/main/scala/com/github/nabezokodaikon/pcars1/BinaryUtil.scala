package com.github.nabezokodaikon.pcars1

import com.typesafe.scalalogging.LazyLogging
import java.nio.ByteBuffer

object SharedMemoryConstants {

  val FRAME_TYPEAND_SEQUENCE: Int = 2

  val PCARS_DATA: Byte = 4
  val SEPARATION: Int = 0

  val MONITOR_STATES_TOTAL_STATES: Int = 9

  val STORED_PARTICIPANTS_MAX: Int = 64
  val STRING_LENGTH_MAX: Int = 64

  // val VEX_X: Int = 0
  // val VEX_Y: Int = 1
  // val VEX_Z: Int = 2

  val PARTICIPANT_INFO_EMPTY = ParticipantInfo(
    worldPosition = Array.fill(3)(0),
    currentLapDistance = 0,
    racePosition = 0,
    lapsCompleted = 0,
    currentLap = 0,
    sector = 0,
    lastSectorTime = 0f)
}

case class ParticipantInfo(
  worldPosition: Array[Short],
  currentLapDistance: Short,
  racePosition: Int,
  lapsCompleted: Int,
  currentLap: Int,
  sector: Int,
  lastSectorTime: Float)

object BinaryUtil extends LazyLogging {

  def readByte(data: List[Byte]): Option[(Byte, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some((byte1, tail))
      case _ => None
    }
  }

  def readUByte(data: List[Byte]): Option[(Int, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(((byte1 << 24) >>> 24, tail))
      case _ => None
    }
  }

  def readChar(data: List[Byte]): Option[(Char, List[Byte])] = {
    data match {
      case byte1 :: byte2 :: tail =>
        if (byte2 == -1) {
          None
        } else {
          Some(((((byte2 << 24) >>> 16) + ((byte1 << 24) >>> 24)).toChar, tail))
        }
      case _ => None
    }
  }

  def readCharArray(data: List[Byte], stringLength: Int): (String, List[Byte]) = {

    def go(currentChars: List[Char], currentData: List[Byte], currentCount: Int): List[Char] = {
      readChar(currentData) match {
        case Some((nextChar, nextData)) =>
          if (currentCount < 1) {
            currentChars
          } else {
            val nextChars = currentChars :+ nextChar
            val nextCount = currentCount - 1
            go(nextChars, nextData, nextCount)
          }
        case None => currentChars
      }
    }

    (new String(go(List[Char](), data, stringLength).toArray), data.drop(stringLength * 2))
  }

  def readCharArray2(data: List[Byte], stringLength: Int): (String, List[Byte]) = {

    val chars = Array.fill(stringLength)(0.toChar)
    var currentData = data
    for (i <- 0 to stringLength - 1) {
      readChar(currentData) match {
        case Some((v, d)) =>
          chars(i) = v
          currentData = d
        case None => ()
      }
    }

    (new String(chars), currentData)
  }

  def readBoolean(data: List[Byte]): Option[(Boolean, List[Byte])] = {
    data match {
      case byte :: tail =>
        if (byte == -1) {
          None
        }
        Some((byte != 0, tail))
      case _ => None
    }
  }

  def readShort(data: List[Byte]): Option[(Short, List[Byte])] =
    data match {
      case byte1 :: byte2 :: tail =>
        if (byte2 == -1) {
          None
        }
        Some(((((byte2 << 24) >>> 16) + (byte1 << 24) >>> 24).toShort, tail))
      case _ => None
    }

  def readInt(data: List[Byte]): Option[(Int, List[Byte])] =
    data match {
      case byte1 :: byte2 :: byte3 :: byte4 :: tail =>
        if (byte4 == -1) {
          None
        }
        Some(((byte4 << 24)
          + ((byte3 << 24) >>> 8)
          + ((byte2 << 24) >>> 16)
          + ((byte1 << 24) >>> 24), tail))
      case _ => None
    }

  def readFloat(data: List[Byte]): Option[(Float, List[Byte])] =
    readInt(data) match {
      case Some((v, d)) => Some((java.lang.Float.intBitsToFloat(v), d))
      case None => None
    }
}
