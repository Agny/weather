package ru.agny.weather

import ru.agny.weather.UserType.{DateStamp, Percent, Celsius}

case class HourlyUnit(day: DateStamp, temperature: Celsius, humidity: Percent, wind: Wind)