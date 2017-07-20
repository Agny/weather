package ru.agny.weather.dto

import ru.agny.weather.utils.{DateFormatter, UserType}
import ru.agny.weather.DayUnit
import UserType.DateString

case class WWODay(date: DateString, maxtempC: String, mintempC: String, hourly: Vector[WWOHour]) {
  def toDayUnit: DayUnit = DayUnit(DateFormatter.dateToDays(date), hourly.map(_.toHourlyUnit))
}
