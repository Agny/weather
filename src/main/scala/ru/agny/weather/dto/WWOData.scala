package ru.agny.weather.dto

import ru.agny.weather.UnprocessedData

case class WWOData(weather: Vector[WWODay]) {
  def toDataHolder: UnprocessedData = UnprocessedData(weather.map(_.toDayUnit))
}
