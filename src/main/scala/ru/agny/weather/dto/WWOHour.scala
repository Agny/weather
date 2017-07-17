package ru.agny.weather.dto

import ru.agny.weather.utils.UserType
import ru.agny.weather.{Wind, HourlyUnit}
import UserType.HourString

case class WWOHour(time: HourString, tempC: String, windspeedKmph: String, winddirDegree: String, humidity: String) {
  def toHourlyUnit: HourlyUnit = HourlyUnit(time, tempC.toInt, humidity.toInt, Wind(windspeedKmph.toInt, winddirDegree.toInt))
}
