package ru.agny.weather

import spray.json.DefaultJsonProtocol

case class UnprocessedData(data: Vector[DayUnit]) {
  def byPeriod(period: PeriodType): UnprocessedData = {
    period match {
      case Whole => this
      case DayTime => UnprocessedData(data.map(_.dayTime))
      case NightTime => UnprocessedData(data.map(_.nightTime))
    }
  }
}

object UnprocessedData extends DefaultJsonProtocol {
  private implicit val wind = jsonFormat2(Wind)
  private implicit val hourlyUnit = jsonFormat4(HourlyUnit.apply)
  private implicit val dayUnit = jsonFormat2(DayUnit)
  implicit val format = jsonFormat1(UnprocessedData.apply)
}