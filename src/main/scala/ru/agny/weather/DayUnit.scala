package ru.agny.weather

import ru.agny.weather.utils.DateFormatter
import ru.agny.weather.utils.UserType._

case class DayUnit(date: Days, hours: Vector[HourlyUnit]) {
  def dayTime: DayUnit = DayUnit(date, hours.filter(_.isDayTime))

  def nightTime: DayUnit = DayUnit(date, hours.filter(!_.isDayTime))
}

case class HourlyUnit(time: HourString, temperature: Celsius, humidity: Percent, wind: Wind) {

  import HourlyUnit._

  def isDayTime: Boolean = {
    val currentTime = DateFormatter.timeToHours(time)
    currentTime >= dayStart && currentTime < dayEnd
  }
}

case class Wind(speed: Kmph, direction: Direction)

object HourlyUnit {
  val (dayStart, dayEnd) = (6, 21)
}