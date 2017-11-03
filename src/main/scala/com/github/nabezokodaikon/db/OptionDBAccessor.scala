package com.github.nabezokodaikon.db

import com.github.nabezokodaikon.util.FileUtil
import com.typesafe.scalalogging.LazyLogging
import org.mapdb.{ DB, DBException, DBMaker, Serializer }
import scala.util.control.Exception.catching

final class OptionDBAccessor(file: String) extends LazyLogging {

  private val db: DB = open()

  val option: OptionMap = new OptionMap(db, "option")

  private def open(): DB =
    catching(classOf[DBException.DataCorruption]).either {
      DBMaker.fileDB(file).make()
    } match {
      case Left(e) =>
        logger.error(e.getMessage)
        logger.debug("OptionDBAccessor reopen.")
        FileUtil.delete(file)
        DBMaker.fileDB(file).make()
      case Right(db) =>
        logger.debug("OptionDBAccessor open.")
        db
    }

  def close(): Unit = {
    db.close()
    logger.debug("OptionDBAccessor close.")
  }
}

trait baseMap {
  val db: DB
  val name: String
}

final class OptionMap(val db: DB, val name: String) extends baseMap {

  val booleanMap = db.hashMap(s"${name}/booleanMap")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.BOOLEAN)
    .createOrOpen()
}
