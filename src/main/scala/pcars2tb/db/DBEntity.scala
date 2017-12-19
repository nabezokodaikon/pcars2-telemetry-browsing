package pcars2tb.db

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

trait DBEntityJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val unitOptionFormat = jsonFormat2(UnitOption)
  implicit val allOptionsFormat = jsonFormat3(AllOptions)

  implicit val buttonIndexFormat = jsonFormat1(ButtonIndex)
  implicit val buttonMappingFormat = jsonFormat2(ButtonMapping)
  implicit val buttonBoxFormat = jsonFormat1(ButtonBox)
}

/*
 * key: option/
 *        isCelsius
 *        isMeter
 *        isBar
 */
final case class UnitOption(key: String, value: Boolean)

final case class AllOptions(
    isCelsius: UnitOption,
    isMeter: UnitOption,
    isBar: UnitOption
)

/*
 * key: buttonBox/
 *        index(0 to 20)/
 *          char: 0 to 9 and A to Z
 *          label: Button label text.
 */
final case class ButtonIndex(index: Int)

final case class ButtonMapping(char: String, label: String)

final case class ButtonBox(mappings: Array[ButtonMapping])

final object ButtonBoxUtil {

  def toCharKey(index: Int): String = s"buttonBox/${index}/char"

  def toLabelKey(index: Int): String = s"buttonBox/${index}/label"

  def toKeyCode(char: String): Option[Int] = {
    import java.awt.event.KeyEvent._
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
  }

  val defaultMappings: Array[ButtonMapping] =
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
}
