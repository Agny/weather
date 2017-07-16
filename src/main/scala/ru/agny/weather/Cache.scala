package ru.agny.weather

import ru.agny.weather.UnitType.{DateStamp, Location}

import scala.concurrent.Future

trait Cache {
  def put(key: (Location, (DateStamp, DateStamp)), value: Vector[HourlyUnit]): Future[Boolean]

  def get(key: (Location, (DateStamp, DateStamp))): Future[Vector[HourlyUnit]]

}
