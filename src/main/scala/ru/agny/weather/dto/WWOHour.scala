package ru.agny.weather.dto

import ru.agny.weather.{Wind, HourlyUnit}
import ru.agny.weather.UserType.HourString

case class WWOHour(time: HourString, tempC: String, windspeedKmph: String, winddirDegree: String, humidity: String) {
  def toHourlyUnit: HourlyUnit = HourlyUnit(time, tempC.toInt, humidity.toInt, Wind(windspeedKmph.toInt, winddirDegree.toInt))
}
