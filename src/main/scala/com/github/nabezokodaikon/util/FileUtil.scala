package com.github.nabezokodaikon.util

import com.github.nabezokodaikon.util.Loan.using
import scala.io.Source

object FileUtil {

  val enc = "UTF-8"

  def read(name: String): String = {
    using(Source.fromFile(name, enc)) { buf =>
      buf.mkString
    }
  }
}
