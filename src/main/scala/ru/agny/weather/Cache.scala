package ru.agny.weather

import ru.agny.weather.UserType.{DateStamp, Location}

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

trait Cache {

  def put(key: CacheKey, value: Vector[HourlyUnit]): Future[Boolean]

  def get(key: CacheKey): Future[Option[Vector[HourlyUnit]]]
}

case class CacheKey(loc: Location, from: DateStamp, to: DateStamp)

object Cache {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit object SimpleCache extends Cache {
    private val content = new TrieMap[CacheKey, Vector[HourlyUnit]]()

    override def put(key: CacheKey, value: Vector[HourlyUnit]): Future[Boolean] = Future {
      content.put(key, value).map(_ => true).getOrElse(true)
    }

    override def get(key: CacheKey): Future[Option[Vector[HourlyUnit]]] = Future {
      content.get(key) match {
        case Some(v) => Some(v)
        case None => isKeyInRange(key) match {
          case Some((_, v)) => Some(shrinkInRequest(key.from, key.to, v))
          case None => None
        }
      }
    }

    private def isKeyInRange(key: CacheKey): Option[(CacheKey, Vector[HourlyUnit])] = {
      content.find(x => x._1.loc == key.loc &&
        x._1.from <= key.from &&
        x._1.to >= key.to
      )
    }

    private def shrinkInRequest(lowerBound: DateStamp, upperBound: DateStamp, from: Vector[HourlyUnit]): Vector[HourlyUnit] = {
      from.dropWhile(_.day < lowerBound).takeWhile(_.day <= upperBound)
    }
  }
}
