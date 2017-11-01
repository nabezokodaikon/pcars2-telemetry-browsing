package com.github.nabezokodaikon.pcars2

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.TelemetryDataConst._
import com.github.nabezokodaikon.util.BigDecimalSupport._

object UDPDataReader {

  def readPacketBase(data: List[Byte]): PacketBase = {
    val (packetNumber, categoryPacketNumberData) = readUInt(data)
    val (categoryPacketNumber, partialPacketIndexData) = readUInt(categoryPacketNumberData)
    val (partialPacketIndex, partialPackatNumberData) = readUByte(partialPacketIndexData)
    val (partialPackatNumber, packetTypeData) = readUByte(partialPackatNumberData)
    val (packetType, packetVersionData) = readUByte(packetTypeData)
    val (packetVersion, lastData) = readUByte(packetVersionData)

    println(lastData.length)

    PacketBase(
      packetNumber = packetNumber,
      categoryPacketNumber = categoryPacketNumber,
      partialPacketIndex = partialPacketIndex,
      partialPacketNumber = partialPackatNumber,
      packetType = packetType,
      packetVersion = packetVersion
    )
  }
}
