package ru.agny.weather

import ru.agny.weather.UserType.{Percent, Celsius}

case class HourlyUnit(temperature: Celsius, humidity: Percent, wind: Wind)