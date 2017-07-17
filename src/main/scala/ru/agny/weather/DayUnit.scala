package ru.agny.weather

import ru.agny.weather.utils.UserType._

case class DayUnit(date: DateStamp, hours: Vector[HourlyUnit])

case class HourlyUnit(time: HourString, temperature: Celsius, humidity: Percent, wind: Wind)

case class Wind(speed: Kmph, direction: Degree)