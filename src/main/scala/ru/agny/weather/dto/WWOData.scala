package ru.agny.weather.dto

import java.text.DateFormat

import ru.agny.weather.UnprocessedData

case class WWOData(weather: Vector[WWODay]) {
  def toDataHolder(implicit format: DateFormat): UnprocessedData = UnprocessedData(weather.map(_.toDayUnit))
}
