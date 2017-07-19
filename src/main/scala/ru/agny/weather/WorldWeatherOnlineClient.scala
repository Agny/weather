package ru.agny.weather

import java.text.{DateFormat, SimpleDateFormat}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import ru.agny.weather.utils.{DateFormatter, ConfigLoader}
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

  override def get(request: ApiRequest): Future[Either[Error, StatData]] = {
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

  private def requestData(request: ApiRequest): Future[Either[Error, WorldWeatherOnlineResponse]] = {
    for {
      response <- Http().singleRequest(HttpRequest(uri = buildUrl(request)))
      units <- unmarshall(response)
    } yield units
  }

  private def buildUrl(r: ApiRequest): String = {
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

  private def lookInCache(r: ApiRequest)(implicit cache: Cache, format: DateFormat): Future[Option[StatData]] = {
    val range = stepByDay(r.from, r.to).map(CacheKey(r.loc, _))
    Future.sequence(range.map(cache.get)).map(x =>
      if (isAllKeysExists(x)) Some(StatData(x.flatten))
      else None
    )
  }

  private def isAllKeysExists(v: Vector[Option[DayUnit]]): Boolean = !v.contains(None)

  private def persistInCache(r: ApiRequest, data: WWOData)(implicit cache: Cache, format: DateFormat): Future[Boolean] = {
    val putResult = data.weather.map(x => (CacheKey(r.loc, x.date), x.toDayUnit)).map(kv => cache.put(kv._1, kv._2))
    Future.sequence(putResult).map(!_.contains(false))
  }
}
