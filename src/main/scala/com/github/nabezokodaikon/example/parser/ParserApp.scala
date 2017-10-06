package com.github.nabezokodaikon.example

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.SharedMemoryConstants._
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171005120800046.bin"
  var mVersionData = FileUtil.readBinary(name).toList
  // println(mVersionData.toString)

  // Version Number
  val (mVersion, mBuildVersionNumberData) = readInt(mVersionData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mVersion.toString)

  val (mBuildVersionNumber, mGameStateData) = readInt(mBuildVersionNumberData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mBuildVersionNumber.toString)

  // Game States
  val (mGameState, mSessionStateData) = readInt(mGameStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mGameState.toString)

  val (mSessionState, mRaceStateData) = readInt(mSessionStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mSessionState.toString)

  val (mRaceState, mViewedParticipantIndexData) = readInt(mRaceStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mRaceState.toString)

  // Participant Info
  val (mViewedParticipantIndex, mNumParticipantsData) = readInt(mViewedParticipantIndexData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mViewedParticipantIndex.toString)

  val (mNumParticipants, mIsActiveData) = readInt(mNumParticipantsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mNumParticipants.toString)

  // ParticipantInfo
  val (mIsActive, mNameData) = readBoolean(mIsActiveData) match {
    case Some((v, d)) => (v, d)
    case None => (false, Nil)
  }
  println(mIsActive.toString)

  val mName = Encoding.decodeString(mNameData.take(STRING_LENGTH_MAX).toArray)
  val mWorldPositionData = mNameData.drop(STRING_LENGTH_MAX * 2)
  println(mName.map(_.toString))
}
