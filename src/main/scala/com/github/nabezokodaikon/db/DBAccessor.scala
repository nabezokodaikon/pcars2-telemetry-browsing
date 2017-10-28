package com.github.nabezokodaikon.db

import com.github.nabezokodaikon.util.FileUtil
import java.util.concurrent.ConcurrentMap
import org.mapdb.{ DB, DBException, DBMaker, Serializer }

final class DBAccessor(file: String) {

  private val db: DB = open()

  val option: OptionMap = new OptionMap(db, "option")

  private def open(): DB = {
    try {
      DBMaker.fileDB(file).make()
    } catch {
      case e: DBException.DataCorruption => {
        FileUtil.delete(file)
        DBMaker.fileDB(file).make()
      }
    }
  }

  def close(): Unit = {
    println("Call close.")
    db.close()
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
