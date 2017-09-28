package com.github.nabezokodaikon

import org.scalatest.FunSuite

class MainSpec extends FunSuite {
  test("helloWorld") {
    assert(Main.helloWorld("nabezokodaikokn") == "Hello nabezokodaikokn!")
  }
}
