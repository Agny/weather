package ru.agny.weather.dto

import spray.json.DefaultJsonProtocol

case class WorldWeatherOnlineResponse(data: WWOData)

object WorldWeatherOnlineResponse extends DefaultJsonProtocol {
  private implicit val wwoHour = jsonFormat5(WWOHour)
  private implicit val wwoDay = jsonFormat4(WWODay)
  private implicit val wwoData = jsonFormat1(WWOData)
  implicit val wwoResponse = jsonFormat1(WorldWeatherOnlineResponse.apply)
}
