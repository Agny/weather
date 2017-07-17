package ru.agny.weather.utils

import ru.agny.weather.utils.UserType.DateString
import spray.json.DefaultJsonProtocol

case class ClientConfig(apiUrl: String, query: QueryParamNames)

case class QueryParamNames(apiKey: String, city: String, from: DateString, to: DateString)

object ClientConfig extends DefaultJsonProtocol {

  private implicit val queryFormat = jsonFormat4(QueryParamNames)
  implicit val format = jsonFormat2(ClientConfig.apply)

  val default = ClientConfig(
    "http://api.worldweatheronline.com/premium/v1/past-weather.ashx",
    QueryParamNames("key", "q", "date", "enddate")
  )
}
