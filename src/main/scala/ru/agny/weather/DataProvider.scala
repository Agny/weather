package ru.agny.weather

import scala.concurrent.Future

trait DataProvider {
  def get(request: ApiRequest): Future[Either[Error, StatData]]
}
