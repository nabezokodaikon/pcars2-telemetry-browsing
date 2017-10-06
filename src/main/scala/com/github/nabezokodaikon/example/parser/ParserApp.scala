package com.github.nabezokodaikon.example

import ch.ethz.acl.passera.unsigned._
import com.github.nabezokodaikon.util.{ BinaryUtil, SharedMemoryConstants }
import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging

object ParserApp extends App with LazyLogging {

  val dir = FileUtil.getCurrentDirectory()
  val name = s"${dir}/testdata/20171005120800046.bin"
  var mVersionData = FileUtil.readBinary(name).toList
  // println(mVersionData.toString)

  // Version Number
  val (mVersion, mBuildVersionNumberData) = BinaryUtil.readInt(mVersionData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mVersion.toString)

  val (mBuildVersionNumber, mGameStateData) = BinaryUtil.readInt(mBuildVersionNumberData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mBuildVersionNumber.toString)

  // Game States
  val (mGameState, mSessionStateData) = BinaryUtil.readInt(mGameStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mGameState.toString)

  val (mSessionState, mRaceStateData) = BinaryUtil.readInt(mSessionStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mSessionState.toString)

  val (mRaceState, mViewedParticipantIndexData) = BinaryUtil.readInt(mRaceStateData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mRaceState.toString)

  // Participant Info
  val (mViewedParticipantIndex, mNumParticipantsData) = BinaryUtil.readInt(mViewedParticipantIndexData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mViewedParticipantIndex.toString)

  val (mNumParticipants, mIsActiveData) = BinaryUtil.readInt(mNumParticipantsData) match {
    case Some((v, d)) => (v, d)
    case None => (0, Nil)
  }
  println(mNumParticipants.toString)

  // ParticipantInfo
  val (mIsActive, mNameData) = BinaryUtil.readBoolean(mIsActiveData) match {
    case Some((v, d)) => (v, d)
    case None => (false, Nil)
  }
  println(mIsActive.toString)

  val (mName, mWorldPositionData) = BinaryUtil.readCharArray(mNameData, SharedMemoryConstants.STRING_LENGTH_MAX)
  println(mName)
}
