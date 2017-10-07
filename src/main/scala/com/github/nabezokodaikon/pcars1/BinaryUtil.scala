package com.github.nabezokodaikon.pcars1

import com.typesafe.scalalogging.LazyLogging
import java.nio.ByteBuffer

object SharedMemoryConstants {
  val STORED_PARTICIPANTS_MAX = 64
  val STRING_LENGTH_MAX: Int = 64

  val VEX_X = 0
  val VEX_Y = 1
  val VEX_Z = 2

  val PARTICIPANT_INFO_EMPTY = ParticipantInfo(
    mIsActive = false,
    mName = Array.fill(64)(" ").mkString,
    mWorldPosition_VEX_X = 0f,
    mWorldPosition_VEX_Y = 0f,
    mWorldPosition_VEX_Z = 0f,
    mCurrentLapDistance = 0f,
    mRacePosition = 0,
    mLapsCompleted = 0,
    mCurrentLap = 0,
    mCurrentSector = 0)
}

case class ParticipantInfo(
  mIsActive: Boolean,
  mName: String, // [ string ]
  mWorldPosition_VEX_X: Float, // [ UNITS = World Space  X  Y  Z ]
  mWorldPosition_VEX_Y: Float, // [ UNITS = World Space  X  Y  Z ]
  mWorldPosition_VEX_Z: Float, // [ UNITS = World Space  X  Y  Z ]
  mCurrentLapDistance: Float, // [ UNITS = Metres ]   [ RANGE = 0.0f->... ]    [ UNSET = 0.0f ]
  mRacePosition: Int, // [ RANGE = 1->... ]   [ UNSET = 0 ]
  mLapsCompleted: Int, // [ RANGE = 0->... ]   [ UNSET = 0 ]
  mCurrentLap: Int, // [ RANGE = 0->... ]   [ UNSET = 0 ]
  mCurrentSector: Int // [ enum (Type#4) Current Sector ]
)

object BinaryUtil extends LazyLogging {

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

  def readCharArray(data: List[Byte], stringLength: Int): (Array[Char], List[Byte]) = {

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

    (go(List[Char](), data, stringLength).toArray, data.drop(stringLength * 2))
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

  // public final float readFloat() throws IOException {
  // return Float.intBitsToFloat(this.readInt());
  // }

  def readFloat(data: List[Byte]): Option[(Float, List[Byte])] =
    readInt(data) match {
      case Some((v, d)) => Some((java.lang.Float.intBitsToFloat(v), d))
      case None => None
    }
}
