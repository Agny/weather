package ru.agny.weather

import ru.agny.weather.UserType.{HourString, DateStamp, Percent, Celsius}

case class DayUnit(date: DateStamp, hours: Vector[HourlyUnit])

case class HourlyUnit(time: HourString, temperature: Celsius, humidity: Percent, wind: Wind)