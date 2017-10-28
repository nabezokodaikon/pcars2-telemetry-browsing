package com.github.nabezokodaikon.db

import java.util.concurrent.ConcurrentMap
import org.mapdb.{ DB, DBMaker, Serializer }

final class DBAccessor(file: String) {

  val db: DB = DBMaker.fileDB(file).make()

  val option: OptionMap = new OptionMap(db, "option")

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
