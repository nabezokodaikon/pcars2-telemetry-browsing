/*
package pcars2tb.example.robot

import com.typesafe.scalalogging.LazyLogging
import java.awt.AWTException
import java.awt.event.KeyEvent._
import java.awt.Robot
import java.lang.IllegalArgumentException
import java.lang.SecurityException
import scala.util.control.Exception.catching

object RobotApp extends App with LazyLogging {

  val robot = catching(classOf[AWTException], classOf[SecurityException]).either {
    new Robot()
  } match {
    case Left(e) =>
      logger.error(e.getMessage)
      None
    case Right(robot) => Some(robot)
  }

  val str = io.StdIn.readLine()
  for (r <- robot) {
    catching(classOf[IllegalArgumentException]).either {
      // r.delay(3000)
      r.keyPress(VK_1)
      r.keyRelease(VK_1)
    } match {
      case Left(e) => logger.error(e.getMessage)
      case _ => Unit
    }
  }
}
*/
