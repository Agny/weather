package ru.agny.weather.dto

import java.text.DateFormat

import ru.agny.weather.StatData

case class WWOData(weather: Vector[WWODay]) {
  def toStatData(implicit format: DateFormat): StatData = StatData(weather.map(_.toDayUnit))
}
