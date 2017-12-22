package pcars2tb.buttonbox

import akka.actor.Actor
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.LazyLogging
import java.awt.AWTException
import java.awt.event.KeyEvent._
import java.awt.Robot
import java.lang.IllegalArgumentException
import java.lang.SecurityException
import pcars2tb.db.ButtonBoxMapDBAccessor
import pcars2tb.util.Loan.using
import scala.util.control.Exception.catching
import spray.json.DefaultJsonProtocol

trait ButtonBoxJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val buttonIndexFormat = jsonFormat1(ButtonIndex)
  implicit val buttonCharFormat = jsonFormat2(ButtonChar)
  implicit val buttonLabelFormat = jsonFormat2(ButtonLabel)
  implicit val buttonMappingFormat = jsonFormat2(ButtonMapping)
  implicit val buttonMappingsFormat = jsonFormat1(ButtonMappings)
}

/*
 * key: index(0 to 20)/
 *        char: 0 to 9 and A to Z.
 *        label: Button label text.
 */
final case class ButtonIndex(index: Int)
final case class ButtonChar(index: Int, char: String)
final case class ButtonLabel(index: Int, label: String)
final case class ButtonMapping(char: String, label: String)
final case class ButtonMappings(mappings: Array[ButtonMapping])

final object ButtonBox extends LazyLogging {

  private def toCharKey(index: Int): String = s"${index}/char"

  private def toLabelKey(index: Int): String = s"${index}/label"

  private def toKeyCode(char: String): Option[Int] =
    char.toUpperCase match {
      case "0" => Some(VK_0)
      case "1" => Some(VK_1)
      case "2" => Some(VK_2)
      case "3" => Some(VK_3)
      case "4" => Some(VK_4)
      case "5" => Some(VK_5)
      case "6" => Some(VK_6)
      case "7" => Some(VK_7)
      case "8" => Some(VK_8)
      case "9" => Some(VK_9)
      case "A" => Some(VK_A)
      case "B" => Some(VK_B)
      case "C" => Some(VK_C)
      case "D" => Some(VK_D)
      case "E" => Some(VK_E)
      case "F" => Some(VK_F)
      case "G" => Some(VK_G)
      case "H" => Some(VK_H)
      case "I" => Some(VK_I)
      case "J" => Some(VK_J)
      case "K" => Some(VK_K)
      case "L" => Some(VK_L)
      case "M" => Some(VK_M)
      case "N" => Some(VK_N)
      case "O" => Some(VK_O)
      case "P" => Some(VK_P)
      case "Q" => Some(VK_Q)
      case "R" => Some(VK_R)
      case "S" => Some(VK_S)
      case "T" => Some(VK_T)
      case "U" => Some(VK_U)
      case "V" => Some(VK_V)
      case "W" => Some(VK_W)
      case "X" => Some(VK_X)
      case "Y" => Some(VK_Y)
      case "Z" => Some(VK_Z)
      case _ => None
    }

  private val defaultMappings: Array[ButtonMapping] =
    Array(
      ButtonMapping("0", "KeyCode: 0"),
      ButtonMapping("1", "KeyCode: 1"),
      ButtonMapping("2", "KeyCode: 2"),
      ButtonMapping("3", "KeyCode: 3"),
      ButtonMapping("4", "KeyCode: 4"),
      ButtonMapping("5", "KeyCode: 5"),
      ButtonMapping("6", "KeyCode: 6"),
      ButtonMapping("7", "KeyCode: 7"),
      ButtonMapping("8", "KeyCode: 8"),
      ButtonMapping("9", "KeyCode: 9"),
      ButtonMapping("A", "KeyCode: A"),
      ButtonMapping("B", "KeyCode: B"),
      ButtonMapping("C", "KeyCode: C"),
      ButtonMapping("D", "KeyCode: D"),
      ButtonMapping("E", "KeyCode: E"),
      ButtonMapping("F", "KeyCode: F"),
      ButtonMapping("G", "KeyCode: G"),
      ButtonMapping("H", "KeyCode: H"),
      ButtonMapping("I", "KeyCode: I"),
      ButtonMapping("J", "KeyCode: J"),
      ButtonMapping("K", "KeyCode: K")
    )

  private val robot = catching(classOf[AWTException], classOf[SecurityException]).either {
    new Robot()
  } match {
    case Left(e) =>
      logger.error(e.getMessage)
      None
    case Right(robot) =>
      // TODO: Need adjustment.
      robot.setAutoDelay(200);
      Some(robot)
  }

  def getAllMappings(): ButtonMappings =
    using(new ButtonBoxMapDBAccessor()) { dac =>
      val array = (0 until defaultMappings.length).map(index => {
        val default = defaultMappings(index)
        val char = dac.map.getOrDefault(toCharKey(index), default.char)
        val label = dac.map.getOrDefault(toLabelKey(index), default.label)
        ButtonMapping(char, label)
      }).toArray
      ButtonMappings(array)
    }

  def updateChar(index: Int, char: String): ButtonChar =
    using(new ButtonBoxMapDBAccessor()) { dac =>
      val key = toCharKey(index)
      dac.map.put(key, char)
      ButtonChar(index, char)
    }

  def updateLabel(index: Int, label: String): ButtonLabel =
    using(new ButtonBoxMapDBAccessor()) { dac =>
      val key = toLabelKey(index)
      dac.map.put(key, label)
      ButtonLabel(index, label)
    }

  def callAction(index: Int): Unit =
    using(new ButtonBoxMapDBAccessor()) { dac =>
      val default = defaultMappings(index)
      val charKey = toCharKey(index)
      val char = dac.map.getOrDefault(charKey, default.char)
      val keyCode = toKeyCode(char)

      for {
        r <- robot
        k <- keyCode
      } {
        catching(classOf[IllegalArgumentException]).either {
          // TODO: Test sleep.
          Thread.sleep(3000)

          r.keyPress(k)
          r.keyRelease(k)
        } match {
          case Left(e) => logger.error(e.getMessage)
          case _ => Unit
        }
      }
    }
}

final class ButtonBox extends Actor with LazyLogging {
  import ButtonBox._

  override def preStart() = {
    logger.debug("ButtonBox preStart.");
  }

  override def postStop() = {
    logger.debug("ButtonBox postStop.");
  }

  def receive(): Receive = {
    case "all" => sender ! getAllMappings()
    case ButtonIndex(index) => callAction(index)
    case ButtonChar(index, char) => sender ! updateChar(index, char)
    case ButtonLabel(index, label) => sender ! updateLabel(index, label)
    case _ =>
      logger.warn("ButtonBox received unknown message.")
  }
}
