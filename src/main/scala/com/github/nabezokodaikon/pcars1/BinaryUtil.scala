package com.github.nabezokodaikon.pcars1

object SharedMemoryConstants {

  val FRAME_TYPEAND_SEQUENCE: Int = 2

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

object BinaryUtil {

  def readByte(data: List[Byte]): Option[(Byte, List[Byte])] = {
    data match {
      case byte1 :: tail =>
        Some(byte1, tail)
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
    (byte1 << 24) >>> 24
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
    val shortCount = count * 2
    if (data.length < shortCount) {
      None
    } else {
      Some(
        data.take(shortCount).grouped(2).toList.map(l => _readShort(l(0), l(1))).toArray,
        data.drop(shortCount))
    }
  }

  def _readUShort(byte1: Byte, byte2: Byte): Int = {
    val h = 0x000000FF & byte1
    val l = 0x000000FF & byte2
    h << 8 | l
  }

  def readUShort(data: List[Byte]): Option[(Int, List[Byte])] =
    data match {
      case byte1 :: byte2 :: tail =>
        Some(_readUShort(byte1, byte2), tail)
      case _ => None
    }

  def readUShortArray(data: List[Byte], count: Int): Option[(Array[Int], List[Byte])] = {
    val uShortCount = count * 2
    if (data.length < uShortCount) {
      None
    } else {
      Some(
        data.take(uShortCount).grouped(2).toList.map(l => _readUShort(l(0), l(1))).toArray,
        data.drop(uShortCount))
    }
  }

  def _readFloat(byte1: Byte, byte2: Byte, byte3: Byte, byte4: Byte): Float = {
    ((byte1 & 0xFF)
      | ((byte2 & 0xFF) << 8)
      | ((byte2 & 0xFF) << 16)
      | ((byte3 & 0xFF) << 24)).toFloat
  }

  def readFloat(data: List[Byte]): Option[(Float, List[Byte])] =
    data match {
      case byte1 :: byte2 :: byte3 :: byte4 :: tail =>
        Some(_readFloat(byte1, byte2, byte3, byte4), tail)
      case _ => None
    }

  def readFloatArray(data: List[Byte], count: Int): Option[(Array[Float], List[Byte])] = {
    val uFloatCount = count * 4
    if (data.length < uFloatCount) {
      None
    } else {
      Some(
        data.take(uFloatCount).grouped(4).toList.map(l => _readFloat(l(0), l(1), l(2), l(3))).toArray,
        data.drop(uFloatCount))
    }
  }
}
