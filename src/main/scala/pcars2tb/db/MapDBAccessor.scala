package pcars2tb.db

import com.typesafe.scalalogging.LazyLogging
import org.mapdb.{ DB, DBException, DBMaker, HTreeMap, Serializer }
import pcars2tb.util.FileUtil
import scala.util.control.Exception.catching

trait MapDBAccessor extends LazyLogging {
  val name: String
  private val file: String = s"${FileUtil.currentDirectory}/${name}.db"

  val db: DB = {
    println(file)
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
  }

  def close(): Unit = {
    db.close()
    logger.debug("OptionDBAccessor close.")
  }
}

final class OptionMapDBAccessor(val name: String = "option")
  extends MapDBAccessor {

  val unitMap: HTreeMap[String, java.lang.Boolean] = db.hashMap(s"${name}")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.BOOLEAN)
    .createOrOpen()
}

final class ButtonBoxMapDBAccessor(val name: String = "buttonBox")
  extends MapDBAccessor {

  val map: HTreeMap[String, String] = db.hashMap(s"${name}")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.STRING)
    .createOrOpen()
}
