package ru.agny.weather.utils

import scala.io.Source

object ConfigLoader {

  import spray.json._
  import ClientConfig.format
  import AppConfig.format

  def loadClient(apiName: String): ClientConfig = {
    val configFile = Source.fromResource(apiName)
    if (configFile.nonEmpty) {
      JsonParser(configFile.mkString).convertTo[ClientConfig]
    } else {
      ClientConfig.default
    }
  }

  def loadApp(commons: String): AppConfig = {
    val configFile = Source.fromResource(commons)
    if (configFile.nonEmpty) {
      JsonParser(configFile.mkString).convertTo[AppConfig]
    } else {
      AppConfig.default
    }
  }
}
