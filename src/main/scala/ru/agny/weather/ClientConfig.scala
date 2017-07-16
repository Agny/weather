package ru.agny.weather

import ru.agny.weather.UserType.DateString

case class ClientConfig(apiUrl: String, query: QueryParamNames)

case class QueryParamNames(city: String, from: DateString, to: DateString)

object ClientConfig {
  val default = ClientConfig(
    "http://api.worldweatheronline.com/premium/v1/weather.ashx",
    QueryParamNames("q", "2015-12-05", "2015-12-08")
  )
}
