package ru.agny.weather

import scala.concurrent.Future

trait DataProvider {
  def get(request: Request): Future[StatData]
}
