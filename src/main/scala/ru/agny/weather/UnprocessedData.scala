package ru.agny.weather

import spray.json.DefaultJsonProtocol

case class UnprocessedData(data: Vector[DayUnit])

object UnprocessedData extends DefaultJsonProtocol {
  private implicit val wind = jsonFormat2(Wind)
  private implicit val hourlyUnit = jsonFormat4(HourlyUnit)
  private implicit val dayUnit = jsonFormat2(DayUnit)
  implicit val format = jsonFormat1(UnprocessedData.apply)
}