package ru.agny.weather.utils

import spray.json.DefaultJsonProtocol

case class AppConfig(host: String, port: Int)

object AppConfig extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(AppConfig.apply)

  val default = AppConfig("localhost", 8080)
}
