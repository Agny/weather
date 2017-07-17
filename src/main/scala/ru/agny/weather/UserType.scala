package ru.agny.weather

import java.text.DateFormat

import ru.agny.weather.UserType.{DateStamp, DateString}

object UserType {
  type Celsius = Int
  type Percent = Int
  type Kmph = Int
  type Degree = Int
  type Location = String
  type DateStamp = Long
  type DateString = String
  type HourString = String
}

object DateFormatter {
  implicit def toStamp(date: DateString)(implicit format:DateFormat): DateStamp = format.parse(date).getTime
}
