package ru.agny.weather

import ru.agny.weather.UserType.DateString

case class ClientConfig(apiUrl: String, query: QueryParamNames)

case class QueryParamNames(apiKey: String, city: String, from: DateString, to: DateString)

object ClientConfig {
  val default = ClientConfig(
    "http://api.worldweatheronline.com/premium/v1/weather.ashx",
    QueryParamNames("key", "q", "date", "enddate")
  )
}
