package pcars2tb

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps

object UsingActor {
  implicit val system = ActorSystem("pcars2-telemetry-browsing")
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)
}
