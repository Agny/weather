package ru.agny.weather

import scala.concurrent.Future

object WorldWeatherOnlineClient extends DataProvider {
  private val config = ConfigLoader.load("world_weather_online")

  override def get(request: Request): Future[StatData] = {
    lookInCache(request).map {
      case Some(v) => Future(v)
      case None =>
        requestData(request: Request).map(x => {
          persistInCache(request, x)
          StatData(x)
        })
    }.flatten
  }

  private def requestData(request: Request): Future[Vector[HourlyUnit]] = ???

  private def lookInCache(request: Request)(implicit cache: Cache): Future[Option[StatData]] = ???

  private def persistInCache(request: Request, data: Vector[HourlyUnit])(implicit cache: Cache): Future[Boolean] = ???
}
