package ru.agny.weather

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

  private val config = ConfigLoader.loadClient("world_weather_online.json")

  override def get(request: ApiRequest): Future[Either[Error, UnprocessedData]] = {
    lookInCache(request).map {
      case Some(v) => Future(Right(v))
      case None => requestData(request).map {
        case Right(v) =>
          persistInCache(request, v.data)
          Right(v.data.toDataHolder)
        case Left(v) => Left(v)
      }
    }.flatten.map(applyPeriodFilter(_, request.periodType))
  }

  private def requestData(request: ApiRequest): Future[Either[Error, WorldWeatherOnlineResponse]] = {
    if (request.isValid) {
      for {
        response <- Http().singleRequest(HttpRequest(uri = buildUri(request)))
        units <- unmarshall(response)
      } yield units
    } else {
      Future.successful(Left(Error(s"Request is incorrect: $request")))
    }
  }

  private def buildUri(r: ApiRequest): Uri = {
    val query = Map(
      config.query.apiKey -> r.apiKey,
      config.query.city -> r.loc,
      config.query.from -> r.from,
      config.query.to -> r.to,
      "format" -> "json",
      "tp" -> "1")
    Uri(config.apiUrl).withQuery(Uri.Query(query))
  }

  private def unmarshall(response: HttpResponse) = {
    Unmarshal(response).to[Either[Error, WorldWeatherOnlineResponse]].recover {
      case t: Throwable => Left(Error(t.toString))
    }
  }

  private def lookInCache(r: ApiRequest)(implicit cache: Cache): Future[Option[UnprocessedData]] = {
    val range = stepByDay(dateToDays(r.from), dateToDays(r.to)).map(CacheKey(r.loc.toUpperCase, _))
    Future.sequence(range.map(cache.get)).map(x =>
      if (isAllKeysExists(x)) Some(UnprocessedData(x.flatten))
      else None
    )
  }

  private def isAllKeysExists(v: Vector[Option[DayUnit]]): Boolean = !v.contains(None)

  private def persistInCache(r: ApiRequest, data: WWOData)(implicit cache: Cache): Future[Boolean] = {
    val putResult = data.weather.map(x => (CacheKey(r.loc.toUpperCase, dateToDays(x.date)), x.toDayUnit)).map(kv => cache.put(kv._1, kv._2))
    Future.sequence(putResult).map(!_.contains(false))
  }

  private def applyPeriodFilter(value: Either[Error, UnprocessedData], period: PeriodType) = value match {
    case Right(v) => Right(v.byPeriod(period))
    case Left(v) => Left(v)
  }
}
