package com.github.nabezokodaikon.util

import akka.http.scaladsl.model.{ ContentTypes, HttpCharsets, MediaTypes }
import akka.http.scaladsl.model.ContentType.WithCharset
import com.github.nabezokodaikon.util.Loan.using
import java.io.File
import scala.io.Source

object FileUtil {

  val enc = "UTF-8"

  def read(name: String): String = {
    using(Source.fromFile(name, enc)) { buf =>
      buf.mkString
    }
  }

  def getCurrentDirectory(): String = {
    new File(".").getAbsoluteFile().getParent()
  }

  def getExtension(file: String): String = {
    file.split('.').last
  }

  def getContentType(file: String): WithCharset = {
    getExtension(file) match {
      case "html" => ContentTypes.`text/html(UTF-8)`
      case "css" => WithCharset(MediaTypes.`text/css`, HttpCharsets.`UTF-8`)
      case "js" => WithCharset(MediaTypes.`application/javascript`, HttpCharsets.`UTF-8`)
      case _ => WithCharset(MediaTypes.`text/plain`, HttpCharsets.`UTF-8`)
    }
  }
}
