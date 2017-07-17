package ru.agny.weather.dto

import java.text.DateFormat

import ru.agny.weather.utils.{DateFormatter, UserType}
import ru.agny.weather.DayUnit
import UserType.DateString

case class WWODay(date: DateString, maxtempC: String, mintempC: String, hourly: Vector[WWOHour]) {
  def toDayUnit(implicit format: DateFormat): DayUnit = DayUnit(DateFormatter.toStamp(date), hourly.map(_.toHourlyUnit))
}
