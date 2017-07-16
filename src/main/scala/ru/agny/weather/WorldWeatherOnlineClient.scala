package ru.agny.weather

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object WorldWeatherOnlineClient extends DataProvider {

  import org.json4s._
  import org.json4s.jackson.JsonMethods._
  import Cache.SimpleCache

  private implicit val formats = DefaultFormats
  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()

  private val config = ConfigLoader.load("world_weather_online.json")

  override def get(request: Request): Future[StatData] = {
    lookInCache(request).map {
      case Some(v) => Future(v)
      case None =>
        requestData(request).map(x => {
          persistInCache(request, x)
          StatData(x)
        })
    }.flatten
  }

  private def requestData(request: Request): Future[Vector[HourlyUnit]] = {
    (for {
      response <- Http().singleRequest(HttpRequest(uri = buildUrl(request)))
      units <- Unmarshal(response).to[String]
    } yield {
      parse(units).extract[Vector[HourlyUnit]]
    }).recover {
      case Throwable =>
        //TODO log error or better move to Either responses
        Vector.empty[HourlyUnit]
    }
  }

  private def buildUrl(r: Request): String = {
    s"${config.apiUrl}" +
      s"?${config.query.apiKey}=${r.apiKey}" +
      s"&${config.query.city}=${r.loc}" +
      s"&${config.query.from}=${r.from}" +
      s"&${config.query.to}=${r.to}" +
      s"&format=json"
  }

  private def lookInCache(r: Request)(implicit cache: Cache): Future[Option[StatData]] = {
    cache.get(CacheKey(r.loc, r.from, r.to)).map(_.map(StatData(_)))
  }

  private def persistInCache(r: Request, data: Vector[HourlyUnit])(implicit cache: Cache): Future[Boolean] =
    cache.put(CacheKey(r.loc, r.from, r.to), data)
}
