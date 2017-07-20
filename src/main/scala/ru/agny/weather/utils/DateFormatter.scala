package ru.agny.weather.utils

import java.time.{LocalTime, LocalDate, ZoneId}
import java.time.format.DateTimeFormatter

import ru.agny.weather.utils.UserType.{Hours, Days, HourString, DateString}

object DateFormatter {

  private val datePattern = "yyyy-MM-dd"
  private val hoursPattern = "HH[mm]"

  private val dateFormat = DateTimeFormatter.ofPattern(datePattern).withZone(ZoneId.of("UTC"))

  private val timeFormat = DateTimeFormatter.ofPattern(hoursPattern).withZone(ZoneId.of("UTC"))

  def dateToDays(date: DateString): Days = {
    LocalDate.from(dateFormat.parse(date)).toEpochDay
  }

  def timeToHours(time: HourString): Hours = {
    LocalTime.from(timeFormat.parse(time)).getHour
  }

  def stepByDay(from: Days, to: Days): Vector[Days] = {
    from to to by 1 toVector
  }
}

