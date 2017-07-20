package ru.agny.weather

import ru.agny.weather.utils.UserType._

case class ApiRequest(apiKey: String, loc: Location, from: DateString, to: DateString, periodType: PeriodType) {
  def isValid: Boolean = apiKey.nonEmpty && loc.nonEmpty && from.nonEmpty && to.nonEmpty
}
