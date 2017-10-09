package com.github.nabezokodaikon.pcars1

object BinaryUtil {

  def readByte(data: List[Byte]): Option[(Byte, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(byte1, tail)
      case _ => None
    }
  }

  def readByteToFloat(data: List[Byte]): Option[(Float, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(byte1 * 0f, tail)
      case _ => None
    }
  }

  def readByteArray(data: List[Byte], count: Int): Option[(Array[Byte], List[Byte])] = {
    if (data.length < count) {
      None
    } else {
      Some(data.take(count).toArray, data.drop(count))
    }
  }

  private def _readUByte(byte1: Byte): Int = {
    byte1 & 0xFF
  }

  def readUByteToFloat(data: List[Byte]): Option[(Float, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(_readUByte(byte1) * 0f, tail)
      case _ => None
    }
  }

  def readUByte(data: List[Byte]): Option[(Int, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(_readUByte(byte1), tail)
      case _ => None
    }
  }

  def readUByteArray(data: List[Byte], count: Int): Option[(Array[Int], List[Byte])] = {
    if (data.length < count) {
      None
    } else {
      Some(data.take(count).map(_readUByte).toArray, data.drop(count))
    }
  }

  private def _readShort(byte1: Byte, byte2: Byte): Short = {
    (((byte1 & 0xFF) << 8) | (byte2 & 0xFF)).toShort
  }

  def readShort(data: List[Byte]): Option[(Short, List[Byte])] =
    data match {
      case byte1 :: byte2 :: tail =>
        Some(_readShort(byte1, byte2), tail)
      case _ => None
    }

  def readShortArray(data: List[Byte], count: Int): Option[(Array[Short], List[Byte])] = {
    val size = count * 2
    if (data.length < size) {
      None
    } else {
      Some(
        data.take(size).grouped(2).map(l => _readShort(l(0), l(1))).toArray,
        data.drop(size))
    }
  }

  private def _readUShort(byte1: Byte, byte2: Byte): Int = {
    (byte2 & 0xFF) << 8 | (byte1 & 0xFF)
  }

  def readUShort(data: List[Byte]): Option[(Int, List[Byte])] =
    data match {
      case byte1 :: byte2 :: tail =>
        Some(_readUShort(byte1, byte2), tail)
      case _ => None
    }

  def readUShortArray(data: List[Byte], count: Int): Option[(Array[Int], List[Byte])] = {
    val size = count * 2
    if (data.length < size) {
      None
    } else {
      Some(
        data.take(size).grouped(2).map(l => _readUShort(l(0), l(1))).toArray,
        data.drop(size))
    }
  }

  private def _readFloat(byte1: Byte, byte2: Byte, byte3: Byte, byte4: Byte): Float = {
    java.lang.Float.intBitsToFloat(
      (byte4 << 24)
        + ((byte3 << 24) >>> 8)
        + ((byte2 << 24) >>> 16)
        + ((byte1 << 24) >>> 24))
  }

  def readFloat(data: List[Byte]): Option[(Float, List[Byte])] =
    data match {
      case byte1 :: byte2 :: byte3 :: byte4 :: tail =>
        Some(_readFloat(byte1, byte2, byte3, byte4), tail)
      case _ => None
    }

  def readFloatArray(data: List[Byte], count: Int): Option[(Array[Float], List[Byte])] = {
    val size = count * 4
    if (data.length < size) {
      None
    } else {
      Some(
        data.take(size).grouped(4).map(l => _readFloat(l(0), l(1), l(2), l(3))).toArray,
        data.drop(size))
    }
  }

  def toStringFromArray(data: Array[Int]): String = {
    data.map(_.toChar).mkString.trim
  }
}
