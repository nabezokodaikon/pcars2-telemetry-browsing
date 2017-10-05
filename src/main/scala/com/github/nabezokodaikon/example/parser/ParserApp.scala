package com.github.nabezokodaikon.example

import ch.ethz.acl.passera.unsigned._
import com.github.nabezokodaikon.util.BinaryUtil
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171005120800046.bin"
  var mVersionData = FileUtil.readBinary(name).toList
  println(mVersionData.toString)

  val (mVersion, mBuildVersionNumberData) = BinaryUtil.readInt(mVersionData)
  // println(mVersion.toString)
  // println(String.valueOf(mVersion).toCharArray())

  val (mBuildVersionNumber, mGameStateData) = BinaryUtil.readInt(mBuildVersionNumberData)
  // println(mBuildVersionNumber.toString)
  // println(String.valueOf(mBuildVersionNumber).toCharArray())
}
