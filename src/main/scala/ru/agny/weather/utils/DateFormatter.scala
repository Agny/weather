package ru.agny.weather.utils

import java.text.DateFormat

import ru.agny.weather.utils.UserType.{DateStamp, DateString}

object DateFormatter {
  private val millisInDay = 24 * 60 * 60 * 1000

  implicit def toStamp(date: DateString)(implicit format: DateFormat): DateStamp = format.parse(date).getTime

  def stepByDay(from: DateString, to: DateString)(implicit format: DateFormat): Vector[DateStamp] = {
    toStamp(from) to toStamp(to) by millisInDay toVector
  }
}
