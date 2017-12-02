package pcars2tb.udp.listener

import akka.actor.{ Actor, ActorRef }
import com.typesafe.scalalogging.LazyLogging
import pcars2tb.udp.factory.{
  FuelDataState,
  FuelDataFactory,
  TimeAggregateFactory,
  TimeAggregateState,
  LapTimeDetailsFactory,
  LapTimeDetailsState
}

final case class FactoryState(
    fuelData: FuelDataState,
    timeAggreate: TimeAggregateState,
    lapTimeDetails: LapTimeDetailsState
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
        lapTimeDetails = LapTimeDetailsState.createInitialState(udpData)
      )))
  }

  private def processing(state: FactoryState): Receive = {
    case udpData: UdpData =>
      val nextState = FactoryState(
        fuelData = state.fuelData.createNextState(udpData) match {
          case (s, Some(d)) =>
            clientManager ! d
            s
          case (s, None) => s
        },
        timeAggreate = state.timeAggreate.createNextState(udpData) match {
          case (s, Some(d)) =>
            clientManager ! d
            s
          case (s, None) => s
        },
        lapTimeDetails = state.lapTimeDetails.createNextState(udpData) match {
          case (s, Some(d)) =>
            clientManager ! d
            s
          case (s, None) => s
        }
      )
      context.become(processing(nextState))
  }
}
