package com.github.nabezokodaikon.util

import ch.ethz.acl.passera.unsigned._
import com.typesafe.scalalogging.LazyLogging
import java.nio.ByteBuffer

object SharedMemoryConstants {
  val STRING_LENGTH_MAX: Int = 64
}

object BinaryUtil extends LazyLogging {

  // public boolean readBoolean() throws IOException {

  // int bool = in.read();
  // if (bool == -1) throw new EOFException();
  // return (bool != 0);

  // }

  // public char readChar() throws IOException {

  // int byte1 = in.read();
  // int byte2 = in.read();
  // if (byte2 == -1) throw new EOFException();
  // return (char) (((byte2 << 24) >>> 16) + ((byte1 << 24) >>> 24));

  // }

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

  def readCharArray(data: List[Byte], stringLength: Int): (List[Char], List[Byte]) = {

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

    (go(List[Char](), data, stringLength), data.drop(stringLength * 2))
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
}
