package com.github.nabezokodaikon.dataListener

import akka.actor.{ Actor, ActorRef }
import com.github.nabezokodaikon.udpListener.{
  UdpStreamerPacketHandlerType,
  PacketBase,
  GameStateDefineValue,
  GameStateData,
  TelemetryData,
  RaceData,
  ParticipantInfo,
  TimingsData,
  ParticipantStatsInfo,
  TimeStatsData,
  AggregateTime
}
import com.github.nabezokodaikon.util.BigDecimalSupport._
import com.typesafe.scalalogging.LazyLogging

final object TotalTimeStorage {
  val base = PacketBase(
    packetNumber = 0,
    categoryPacketNumber = 0,
    partialPacketIndex = 0,
    partialPacketNumber = 0,
    packetType = UdpStreamerPacketHandlerType.AGGREGATE_TIME,
    packetVersion = 0,
    dataTimestamp = System.currentTimeMillis,
    dataSize = 0
  )
}

final case class TotalTimeStorage(
    isMenu: Boolean,
    isPlaying: Boolean,
    isRestart: Boolean,
    viewedParticipantIndex: Byte,
    totalTimes: Array[TotalTime]
) {
  import TotalTimeStorage._

  def toTotalTimeData(): AggregateTime = {
    val cumulativeTimes = for {
      i <- totalTimes
      if (i.currentSectorTime > 0f)
    } yield i.currentSectorTime + i.cumulativeTime

    val fastestTime = if (cumulativeTimes.length > 0) cumulativeTimes.min else 0f
    val viewedParticipantTotalTime = totalTimes(viewedParticipantIndex)
    val totalTime = viewedParticipantTotalTime.currentSectorTime + viewedParticipantTotalTime.cumulativeTime
    val gapTime = totalTime - fastestTime

    AggregateTime(
      base = base,
      totalTime = totalTime.toMinuteFormatFromSeconds,
      gapTime = gapTime.toMinuteFormatFromSeconds
    )
  }
}

final object TotalTime {
  val empty: TotalTime = TotalTime(0, 0, 0f, 0f)
  val emptyArray: Array[TotalTime] = Array.fill(32)(empty)
}

final case class TotalTime(
    sector: Short,
    lastSector: Short,
    currentSectorTime: Float,
    cumulativeTime: Float
)

final class AggregateTimeListener(clientManager: ActorRef)
  extends Actor
  with LazyLogging {

  override def preStart() = {
    logger.debug("AggregateTimeListener preStart.");
  }

  override def postStop() = {
    logger.debug("AggregateTimeListener postStop.")
  }

  def receive(): Receive = {
    case udpData: GameStateData =>
      context.become(processing(createInitialStorage(udpData)))
  }

  private def processing(storage: TotalTimeStorage): Receive = {
    case udpData: GameStateData =>
      val nextStorage = resetStorage(storage, udpData)
      context.become(processing(nextStorage))
    case udpData: RaceData if (storage.isMenu) =>
      val nextStorage = resetStorage(storage)
      clientManager ! nextStorage.toTotalTimeData()
      context.become(processing(nextStorage))
    case udpData: TelemetryData if (storage.isPlaying) =>
      if (udpData.participantInfo.viewedParticipantIndex != storage.viewedParticipantIndex) {
        val nextStorage = mergeViewedParticipantIndex(
          storage, udpData.participantInfo.viewedParticipantIndex
        )
        context.become(processing(nextStorage))
      }
    case udpData: TimingsData if (storage.isPlaying) =>
      mergeStorage(storage, udpData) match {
        case Some(nextStorage) =>
          clientManager ! nextStorage.toTotalTimeData()
          context.become(processing(nextStorage))
        case None => Unit
      }
    case udpData: TimeStatsData if (storage.isMenu) =>
      val nextStorage = resetStorage(storage)
      clientManager ! nextStorage.toTotalTimeData()
      context.become(processing(nextStorage))
    case udpData: TimeStatsData if (storage.isRestart) =>
      val nextStorage = resetStorage(storage)
      clientManager ! nextStorage.toTotalTimeData()
      context.become(processing(nextStorage))
    case udpData: TimeStatsData if (storage.isPlaying) =>
      mergeStorage(storage, udpData) match {
        case Some(nextStorage) =>
          clientManager ! nextStorage.toTotalTimeData()
          context.become(processing(nextStorage))
        case None => Unit
      }
  }

  private def createInitialStorage(gameStateData: GameStateData): TotalTimeStorage =
    TotalTimeStorage(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = 0,
      totalTimes = TotalTime.emptyArray
    )

  private def resetStorage(
    storage: TotalTimeStorage, gameStateData: GameStateData
  ): TotalTimeStorage =
    TotalTimeStorage(
      isMenu = (gameStateData.gameState == GameStateDefineValue.GAME_FRONT_END),
      isPlaying = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_PLAYING
        || gameStateData.gameState == GameStateDefineValue.GAME_INGAME_INMENU_TIME_TICKING),
      isRestart = (gameStateData.gameState == GameStateDefineValue.GAME_INGAME_RESTARTING),
      viewedParticipantIndex = storage.viewedParticipantIndex,
      totalTimes = storage.totalTimes
    )

  private def resetStorage(storage: TotalTimeStorage): TotalTimeStorage =
    TotalTimeStorage(
      isMenu = storage.isMenu,
      isPlaying = storage.isPlaying,
      isRestart = storage.isRestart,
      viewedParticipantIndex = storage.viewedParticipantIndex,
      totalTimes = TotalTime.emptyArray
    )

  private def mergeViewedParticipantIndex(
    storage: TotalTimeStorage, viewedParticipantIndex: Byte
  ): TotalTimeStorage =
    TotalTimeStorage(
      isMenu = storage.isMenu,
      isPlaying = storage.isPlaying,
      isRestart = storage.isRestart,
      viewedParticipantIndex = viewedParticipantIndex,
      totalTimes = TotalTime.emptyArray
    )

  private def mergeStorage(
    storage: TotalTimeStorage, timingsData: TimingsData
  ): Option[TotalTimeStorage] = {
    if (storage.totalTimes.length < 32 || timingsData.participants.length < 32) {
      return None
    }

    val participant = timingsData.participants(storage.viewedParticipantIndex)
    if (participant.currentTime < 0) {
      return None
    }

    val totalTimes =
      for (
        i <- 0 to 31
      ) yield mergeTotalTime(storage.totalTimes(i), timingsData.participants(i))

    Some(TotalTimeStorage(
      isMenu = storage.isMenu,
      isPlaying = storage.isPlaying,
      isRestart = storage.isRestart,
      viewedParticipantIndex = storage.viewedParticipantIndex,
      totalTimes = totalTimes.toArray
    ))
  }

  private def mergeTotalTime(
    totalTime: TotalTime, participantInfo: ParticipantInfo
  ): TotalTime =
    TotalTime(
      sector = participantInfo.sector,
      lastSector =
        if (totalTime.sector != participantInfo.sector) totalTime.lastSector
        else totalTime.sector,
      currentSectorTime = participantInfo.currentSectorTime,
      cumulativeTime = totalTime.cumulativeTime
    )

  private def mergeStorage(
    storage: TotalTimeStorage, timeStatsData: TimeStatsData
  ): Option[TotalTimeStorage] = {
    if (storage.totalTimes.length < 32 || timeStatsData.stats.participants.length < 32) {
      return None
    }

    val totalTimes =
      for (
        i <- 0 to 31
      ) yield mergeTotalTime(storage.totalTimes(i), timeStatsData.stats.participants(i))

    Some(TotalTimeStorage(
      isMenu = storage.isMenu,
      isPlaying = storage.isPlaying,
      isRestart = storage.isRestart,
      viewedParticipantIndex = storage.viewedParticipantIndex,
      totalTimes = totalTimes.toArray
    ))
  }

  private def mergeTotalTime(
    totalTime: TotalTime, participantInfo: ParticipantStatsInfo
  ): TotalTime = {
    if (totalTime.sector == totalTime.lastSector) {
      return totalTime
    }

    val lastSectorTime = if (participantInfo.lastSectorTime > 0f) participantInfo.lastSectorTime else 0f

    TotalTime(
      sector = totalTime.sector,
      lastSector = totalTime.lastSector,
      currentSectorTime = totalTime.currentSectorTime,
      cumulativeTime = totalTime.cumulativeTime + lastSectorTime
    )
  }
}
