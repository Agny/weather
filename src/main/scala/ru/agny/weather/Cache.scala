package ru.agny.weather

import ru.agny.weather.utils.UserType.{DateStamp, Location}

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

trait Cache {

  def put(key: CacheKey, value: DayUnit): Future[Boolean]

  def get(key: CacheKey): Future[Option[DayUnit]]
}

case class CacheKey(loc: Location, date: DateStamp)

object Cache {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit object SimpleCache extends Cache {
    private val content = new TrieMap[CacheKey, DayUnit]()

    override def put(key: CacheKey, value: DayUnit): Future[Boolean] = Future {
      content.put(key, value).map(_ => true).getOrElse(true)
    }

    override def get(key: CacheKey): Future[Option[DayUnit]] = Future {
      content.get(key)
    }
  }
}
