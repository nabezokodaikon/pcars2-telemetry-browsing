package com.github.nabezokodaikon.pcars1

object BinaryUtil {

  def readByte(data: List[Byte], default: Byte = 0): (Byte, List[Byte]) =
    data match {
      case byte1 :: tail => (byte1, tail)
      case _ => (default, List[Byte]())
    }

  def readByteArray(data: List[Byte], count: Int): (Array[Byte], List[Byte]) =
    count match {
      case len if len <= data.length =>
        (data.take(len).toArray, data.drop(len))
      case _ => (Array[Byte](), List[Byte]())
    }

  private def _readUByte(byte1: Byte): Int = {
    byte1 & 0xFF
  }

  def readUByte(data: List[Byte], default: Int = 0): (Int, List[Byte]) =
    data match {
      case byte1 :: tail => (_readUByte(byte1), tail)
      case _ => (default, List[Byte]())
    }

  def readUByteArray(data: List[Byte], count: Int): (Array[Int], List[Byte]) =
    count match {
      case len if len <= data.length =>
        (data.take(len).map(_readUByte).toArray, data.drop(len))
      case _ => (Array[Int](), List[Byte]())
    }

  private def _readShort(byte1: Byte, byte2: Byte): Short = {
    (((byte1 & 0xFF) << 8) | (byte2 & 0xFF)).toShort
  }

  def readShort(data: List[Byte], default: Short = 0): (Short, List[Byte]) =
    data match {
      case byte1 :: byte2 :: tail => (_readShort(byte1, byte2), tail)
      case _ => (default, List[Byte]())
    }

  def readShortArray(data: List[Byte], count: Int): (Array[Short], List[Byte]) =
    count * 2 match {
      case len if len <= data.length =>
        (data.take(len).grouped(2).map(l => _readShort(l(0), l(1))).toArray, data.drop(len))
      case _ => (Array[Short](), List[Byte]())
    }

  private def _readUShort(byte1: Byte, byte2: Byte): Int = {
    (byte2 & 0xFF) << 8 | (byte1 & 0xFF)
  }

  def readUShort(data: List[Byte], default: Int = 0): (Int, List[Byte]) =
    data match {
      case byte1 :: byte2 :: tail => (_readUShort(byte1, byte2), tail)
      case _ => (default, List[Byte]())
    }

  def readUShortArray(data: List[Byte], count: Int): (Array[Int], List[Byte]) =
    count * 2 match {
      case len if len <= data.length =>
        (data.take(len).grouped(2).map(l => _readUShort(l(0), l(1))).toArray, data.drop(len))
      case _ => (Array[Int](), List[Byte]())
    }

  private def _readFloat(byte1: Byte, byte2: Byte, byte3: Byte, byte4: Byte): Float = {
    java.lang.Float.intBitsToFloat(
      (byte4 << 24)
        + ((byte3 << 24) >>> 8)
        + ((byte2 << 24) >>> 16)
        + ((byte1 << 24) >>> 24))
  }

  def readFloat(data: List[Byte], default: Float = 0f): (Float, List[Byte]) =
    data match {
      case byte1 :: byte2 :: byte3 :: byte4 :: tail =>
        (_readFloat(byte1, byte2, byte3, byte4), tail)
      case _ => (default, List[Byte]())
    }

  def readFloatArray(data: List[Byte], count: Int): (Array[Float], List[Byte]) =
    count * 4 match {
      case len if len <= data.length =>
        (data.take(len).grouped(4).map(l => _readFloat(l(0), l(1), l(2), l(3))).toArray, data.drop(len))
      case _ => (Array[Float](), List[Byte]())
    }

  def toStringFromArray(data: Array[Int]): String = {
    data.map(_.toChar).mkString.trim
  }
}
