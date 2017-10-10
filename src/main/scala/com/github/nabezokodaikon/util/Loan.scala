package com.github.nabezokodaikon.util

import scala.language.reflectiveCalls

object Loan {

  type TClosable = {
    def close(): Unit
  }

  type TDisposable = {
    def dispose(): Unit
  }

  def d2c(implicit d: TDisposable): TClosable = {
    new { def close() = d.dispose() }
  }

  def using[C <% TClosable, T](h: C)(work: C => T): T = {
    try {
      work(h)
    } finally {
      h.close()
    }
  }

  def runningTime(work: => Unit): Unit = {
    val start = System.currentTimeMillis
    try {
      work
    } finally {
      val interval = System.currentTimeMillis - start
      println(s"${interval} msec")
    }
  }
}
