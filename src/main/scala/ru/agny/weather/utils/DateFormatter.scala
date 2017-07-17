package ru.agny.weather.utils

import java.text.DateFormat

import ru.agny.weather.utils.UserType.{DateStamp, DateString}

object DateFormatter {
  implicit def toStamp(date: DateString)(implicit format: DateFormat): DateStamp = format.parse(date).getTime
}
