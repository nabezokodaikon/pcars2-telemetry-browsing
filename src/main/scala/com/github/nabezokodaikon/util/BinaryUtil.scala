package com.github.nabezokodaikon.util

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

  private def _readUByte(byte1: Byte): Short = {
    (byte1 & 0xFF).toShort
  }

  def readUByte(data: List[Byte], default: Short = 0): (Short, List[Byte]) =
    data match {
      case byte1 :: tail => (_readUByte(byte1), tail)
      case _ => (default, List[Byte]())
    }

  def readUByteArray(data: List[Byte], count: Int): (Array[Short], List[Byte]) =
    count match {
      case len if len <= data.length =>
        (data.take(len).map(_readUByte).toArray, data.drop(len))
      case _ => (Array[Short](), List[Byte]())
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

  private def _readUInt(byte1: Byte, byte2: Byte, byte3: Byte, byte4: Byte): Long = {
    ((byte1 & 0xFF) << 0) |
      ((byte2 & 0xFF) << 8) |
      ((byte3 & 0xFF) << 16) |
      ((byte4 & 0xFF) << 24)
  }

  def readUInt(data: List[Byte], default: Long = 0L): (Long, List[Byte]) =
    data match {
      case byte1 :: byte2 :: byte3 :: byte4 :: tail => (_readUInt(byte1, byte2, byte3, byte4), tail)
      case _ => (default, List[Byte]())
    }

  def readUIntArray(data: List[Byte], count: Int): (Array[Long], List[Byte]) =
    count * 4 match {
      case len if len <= data.length =>
        (data.take(len).grouped(4).map(l => _readUInt(l(0), l(1), l(2), l(3))).toArray, data.drop(len))
      case _ => (Array[Long](), List[Byte]())
    }

  private def _readFloat(byte1: Byte, byte2: Byte, byte3: Byte, byte4: Byte): Float = {
    java.lang.Float.intBitsToFloat(
      (byte4 << 24)
        + ((byte3 << 24) >>> 8)
        + ((byte2 << 24) >>> 16)
        + ((byte1 << 24) >>> 24)
    )
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

  // private def readString(data: Array[Byte]): String =
  // data.map(_.toChar).mkString.trim

  private val NULL_CHAR = new String(Array[Byte](0))

  private def trimEnd(s: String, bad: Char): String =
    s.replaceAll(s"[" + bad + "]+$", "")

  private def readString(data: Array[Byte]): String = {
    if (data.length < 1) {
      return ""
    }

    val firstChar = trimEnd(data.head.toChar.toString, '\u0000')
    if (data.length < 2) {
      return firstChar
    }

    val rest = trimEnd(data.drop(1).map(_.toChar).mkString, '\u0000')

    val f = (firstChar.trim.length == 0 && rest.trim.length == 0) match {
      case true => "?"
      case false => firstChar.trim
    }

    val r = (rest.contains(NULL_CHAR)) match {
      case true => rest.substring(0, rest.indexOf(NULL_CHAR))
      case false => rest
    }

    s"${f}${r}".trim
  }

  def readString(data: List[Byte], stringLength: Int): (String, List[Byte]) =
    readByteArray(data, stringLength) match {
      case (v, d) => (readString(v), d)
      case _ => ("", List[Byte]())
    }

  def readStringArray(data: List[Byte], stringCount: Int, stringLength: Int): (Array[String], List[Byte]) =
    stringCount * stringLength match {
      case len if len <= data.length =>
        (data.take(len).grouped(stringLength).map(d => readString(d, stringLength)._1).toArray, data.drop(len))
      case _ => (Array[String](), List[Byte]())
    }
}
