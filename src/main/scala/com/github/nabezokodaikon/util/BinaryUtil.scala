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
}
