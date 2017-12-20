package pcars2tb.db

import com.typesafe.scalalogging.LazyLogging
import org.mapdb.{ DB, DBException, DBMaker, HTreeMap, Serializer }
import pcars2tb.util.FileUtil
import scala.util.control.Exception.catching

trait MapDBAccessor[T] extends LazyLogging {
  val name: String
  val file: String = s"${FileUtil.currentDirectory}/${name}.db"
  val map: HTreeMap[String, T]

  val db: DB =
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

final class OptionMapDBAccessor
  extends MapDBAccessor[java.lang.Boolean] {

  val name: String = "option"

  val map: HTreeMap[String, java.lang.Boolean] = db.hashMap(s"${name}")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.BOOLEAN)
    .createOrOpen()
}

final class ButtonBoxMapDBAccessor
  extends MapDBAccessor[String] {

  val name: String = "buttonBox"

  val map: HTreeMap[String, String] = db.hashMap(s"${name}")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.STRING)
    .createOrOpen()
}
