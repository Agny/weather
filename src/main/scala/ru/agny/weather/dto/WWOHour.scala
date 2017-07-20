package ru.agny.weather.dto

import ru.agny.weather.utils.UserType
import ru.agny.weather.{Wind, HourlyUnit}
import UserType.HourString

case class WWOHour(time: HourString, tempC: String, windspeedKmph: String, winddir16Point: String, humidity: String) {
  def toHourlyUnit: HourlyUnit = HourlyUnit(formatHour(time), tempC.toInt, humidity.toInt, Wind(windspeedKmph.toInt, winddir16Point))

  private def formatHour(hour: HourString): HourString = {
    if (hour.length == 1) s"0${hour}00"
    else if (hour.length == 3) s"0$hour"
    else hour
  }
}
