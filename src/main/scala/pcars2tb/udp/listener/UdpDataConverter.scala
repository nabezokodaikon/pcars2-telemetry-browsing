package pcars2tb.udp.listener

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.state.{
  FuelDataState,
  TimeAggregateState,
  LapTimeDetailsState,
  RealTimeGapState,
  TelemetrySummaryState
}

final case class FactoryState(
    fuelData: FuelDataState,
    timeAggreate: TimeAggregateState,
    lapTimeDetails: LapTimeDetailsState,
    realTimeGap: RealTimeGapState,
    telemetrySummary: TelemetrySummaryState
)

final class UdpDataConverter(clientManager: ActorRef) extends Actor with LazyLogging {

  override def preStart() = {
    logger.debug("UdpDataConverter preStart.");
  }

  override def postStop() = {
    logger.debug("UdpDataConverter postStop.");
  }

  def receive(): Receive = {
    case udpData: GameStateData =>
      context.become(processing(FactoryState(
        fuelData = FuelDataState.createInitialState(udpData),
        timeAggreate = TimeAggregateState.createInitialState(udpData),
        lapTimeDetails = LapTimeDetailsState.createInitialState(udpData),
        realTimeGap = RealTimeGapState.createInitialState(udpData),
        telemetrySummary = TelemetrySummaryState.createInitialState(udpData)
      )))
  }

  private def processing(state: FactoryState): Receive = {
    case udpData: UdpData =>
      val (fuelData, udpFuelData) = state.fuelData.createNextState(udpData)
      for (d <- udpFuelData) clientManager ! d

      val (timeAggreate, udpTimeAggreate) = state.timeAggreate.createNextState(udpData)
      for (d <- udpTimeAggreate) clientManager ! d

      val (lapTimeDetails, udpLapTimeDetails) = state.lapTimeDetails.createNextState(udpData)
      for (d <- udpLapTimeDetails) clientManager ! d

      val (realTimeGap, udpRealTimeGap) = state.realTimeGap.createNextState(udpData)
      for (d <- udpRealTimeGap) clientManager ! d

      val (telemetrySummary, udpTelemetrySummary) = state.telemetrySummary.createNextState(udpData)
      for (d <- udpTelemetrySummary) clientManager ! d

      val nextState = FactoryState(
        fuelData = fuelData,
        timeAggreate = timeAggreate,
        lapTimeDetails = lapTimeDetails,
        realTimeGap = realTimeGap,
        telemetrySummary = telemetrySummary
      )
      context.become(processing(nextState))
  }
}
