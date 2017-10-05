package com.github.nabezokodaikon.util

import ch.ethz.acl.passera.unsigned._
import com.typesafe.scalalogging.LazyLogging
import java.nio.ByteBuffer

object BinaryUtil extends LazyLogging {

  def readUInt(data: List[Byte]): (UInt, List[Byte]) =
    data match {
      case a :: b :: c :: d :: tail =>
        (ByteBuffer.wrap(Array(a, b, c, d)).getInt.toUInt, tail)
      case _ => (UInt.MinValue, Nil)
    }

  def readInt(data: List[Byte]): (Int, List[Byte]) =
    data match {
      case a :: b :: c :: d :: tail =>
        // val s = new String(Array(a, b, c, d), "US-ASCII")
        // println(s)
        val t = String.valueOf(Array(a, b, c, d))
        println(t)
        (ByteBuffer.wrap(Array(a, b, c, d)).getInt, tail)
      case _ => (' ', Nil)
    }
}
