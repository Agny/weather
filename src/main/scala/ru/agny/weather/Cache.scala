package ru.agny.weather

import ru.agny.weather.UserType.{DateStamp, Location}

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

trait Cache {

  def put(key: CacheKey, value: Vector[DayUnit]): Future[Boolean]

  def get(key: CacheKey): Future[Option[Vector[DayUnit]]]
}

case class CacheKey(loc: Location, from: DateStamp, to: DateStamp)

object Cache {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit object SimpleCache extends Cache {
    private val content = new TrieMap[CacheKey, Vector[DayUnit]]()

    override def put(key: CacheKey, value: Vector[DayUnit]): Future[Boolean] = Future {
      content.put(key, value).map(_ => true).getOrElse(true)
    }

    override def get(key: CacheKey): Future[Option[Vector[DayUnit]]] = Future {
      content.get(key) match {
        case Some(v) => Some(v)
        case None => isKeyInRange(key) match {
          case Some((_, v)) => Some(shrinkInRequest(key.from, key.to, v))
          case None => None
        }
      }
    }

    private def isKeyInRange(key: CacheKey): Option[(CacheKey, Vector[DayUnit])] = {
      content.find(x => x._1.loc == key.loc &&
        x._1.from <= key.from &&
        x._1.to >= key.to
      )
    }

    private def shrinkInRequest(lowerBound: DateStamp, upperBound: DateStamp, from: Vector[DayUnit]): Vector[DayUnit] = {
      from.dropWhile(_.date < lowerBound).takeWhile(_.date <= upperBound)
    }
  }
}
