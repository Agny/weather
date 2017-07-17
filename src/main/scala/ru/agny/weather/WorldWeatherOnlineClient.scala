package ru.agny.weather

import java.text.{DateFormat, SimpleDateFormat}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import ru.agny.weather.utils.{DateFormatter, UserType, ConfigLoader}
import UserType.DateStamp
import ru.agny.weather.dto.{WWOData, WorldWeatherOnlineResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object WorldWeatherOnlineClient extends DataProvider {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import Cache.SimpleCache
  import DateFormatter._
  import WorldWeatherOnlineResponse.wwoResponse
  import Error.format

  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()

  private val config = ConfigLoader.load("world_weather_online.json")

  override def get(request: Request): Future[Either[Error, StatData]] = {
    implicit val format = new SimpleDateFormat("yyyy-MM-dd")
    lookInCache(request).map {
      case Some(v) => Future(Right(v))
      case None => requestData(request).map {
        case Right(v) =>
          persistInCache(request, v.data)
          Right(v.data.toStatData)
        case Left(v) => Left(v)
      }
    }.flatten
  }

  private def requestData(request: Request): Future[Either[Error, WorldWeatherOnlineResponse]] = {
    for {
      response <- Http().singleRequest(HttpRequest(uri = buildUrl(request)))
      units <- unmarshall(response)
    } yield units
  }

  private def buildUrl(r: Request): String = {
    s"${config.apiUrl}" +
      s"?${config.query.apiKey}=${r.apiKey}" +
      s"&${config.query.city}=${r.loc}" +
      s"&${config.query.from}=${r.from}" +
      s"&${config.query.to}=${r.to}" +
      "&format=json" +
      "&tp=1"
  }

  private def unmarshall(response: HttpResponse) = {
    Unmarshal(response).to[Either[Error, WorldWeatherOnlineResponse]].recover {
      case t: Throwable => Left(Error(t.toString))
    }
  }

  private def lookInCache(r: Request)(implicit cache: Cache, format: DateFormat): Future[Option[StatData]] = {
    cache.get(CacheKey(r.loc, r.from, r.to)).map(_.map(StatData(_)))
  }

  private def persistInCache(r: Request, data: WWOData)(implicit cache: Cache, format: DateFormat): Future[Boolean] = {
    val min = data.weather.minBy[DateStamp](_.date)
    val max = data.weather.maxBy[DateStamp](_.date)
    cache.put(CacheKey(r.loc, min.date, max.date), data.weather.map(_.toDayUnit))
  }
}
