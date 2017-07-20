package ru.agny.weather.utils

import java.text.{SimpleDateFormat, DateFormat}
import java.util.TimeZone

import ru.agny.weather.utils.UserType.{DateStamp, DateString}

object DateFormatter {
  private val millisInDay = 24 * 60 * 60 * 1000
  private val dateFormat = "yyyy-MM-dd"

  def createFormatter: DateFormat = {
    val format = new SimpleDateFormat(dateFormat)
    format.setTimeZone(TimeZone.getTimeZone("Utc"))
    format
  }

  implicit def toStamp(date: DateString)(implicit format: DateFormat): DateStamp = format.parse(date).getTime

  def stepByDay(from: DateStamp, to: DateStamp)(implicit format: DateFormat): Vector[DateStamp] = {
    from to to by millisInDay toVector
  }
}
