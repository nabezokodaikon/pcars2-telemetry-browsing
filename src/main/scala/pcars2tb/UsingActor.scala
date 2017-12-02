package pcars2tb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object UsingActor {
  implicit val system = ActorSystem("pcars2-telemetry-browsing")
  implicit val materializer = ActorMaterializer()
}
