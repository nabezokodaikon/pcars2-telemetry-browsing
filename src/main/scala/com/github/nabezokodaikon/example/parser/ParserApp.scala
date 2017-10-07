package com.github.nabezokodaikon.example

import com.github.nabezokodaikon.pcars1.BinaryUtil._
import com.github.nabezokodaikon.pcars1.Encoding
import com.github.nabezokodaikon.pcars1.ParticipantInfo
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
  val participantInfoArray = Array.fill(64)(PARTICIPANT_INFO_EMPTY)
  var participantInfoArrayNextData = mIsActiveData
  for (i <- 0 to 63) {

    val (mIsActive, mNameData) = readBoolean(participantInfoArrayNextData) match {
      case Some((v, d)) => (v, d)
      case None => (false, Nil)
    }

    val mName = Encoding.decodeString(mNameData.take(STRING_LENGTH_MAX).toArray)
    val mWorldPosition_VEX_XData = mNameData.drop(STRING_LENGTH_MAX * 2)

    val (mWorldPosition_VEX_X, mWorldPosition_VEY_XData) = readFloat(mWorldPosition_VEX_XData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (mWorldPosition_VEX_Y, mWorldPosition_VEZ_XData) = readFloat(mWorldPosition_VEY_XData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (mWorldPosition_VEX_Z, mCurrentLapDistanceData) = readFloat(mWorldPosition_VEZ_XData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (mCurrentLapDistance, mRacePositionData) = readFloat(mCurrentLapDistanceData) match {
      case Some((v, d)) => (v, d)
      case None => (0f, Nil)
    }

    val (mRacePosition, mLapsCompletedData) = readInt(mRacePositionData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (mLapsCompleted, mCurrentLapData) = readInt(mLapsCompletedData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (mCurrentLap, mCurrentSectorData) = readInt(mCurrentLapData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    val (mCurrentSector, nextData) = readInt(mCurrentSectorData) match {
      case Some((v, d)) => (v, d)
      case None => (0, Nil)
    }

    participantInfoArray(i) = ParticipantInfo(
      mIsActive,
      mName,
      mWorldPosition_VEX_X,
      mWorldPosition_VEX_Y,
      mWorldPosition_VEX_Z,
      mCurrentLapDistance,
      mRacePosition,
      mLapsCompleted,
      mCurrentLap,
      mCurrentSector)
    println(participantInfoArray(i))

    participantInfoArrayNextData = nextData
  }

  // val (mIsActive_1, mName_1Data) = readBoolean(mIsActive_1Data) match {
  // case Some((v, d)) => (v, d)
  // case None => (false, Nil)
  // }
  // println(mIsActive_1.toString)

  // val mName_1 = Encoding.decodeString(mName_1Data.take(STRING_LENGTH_MAX).toArray)
  // val mWorldPosition_VEX_X_1Data = mName_1Data.drop(STRING_LENGTH_MAX * 2)
  // println(mName_1.map(_.toString))

}
