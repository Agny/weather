package ru.agny.weather.utils

import scala.io.Source

object ConfigLoader {

  import spray.json._
  import ClientConfig.format
  import AppConfig.format

  private val outsideJar = "./"

  def loadClient(apiName: String): ClientConfig = {
    try {
      val configFile = Source.fromResource(outsideJar + apiName)
      if (configFile.nonEmpty) {
        JsonParser(configFile.mkString).convertTo[ClientConfig]
      } else {
        ClientConfig.default
      }
    } catch {
      case _: Throwable => ClientConfig.default
    }
  }

  def loadApp(commons: String): AppConfig = {
    try {
      val configFile = Source.fromResource(outsideJar + commons)
      if (configFile.nonEmpty) {
        JsonParser(configFile.mkString).convertTo[AppConfig]
      } else {
        AppConfig.default
      }
    } catch {
      case _: Throwable => AppConfig.default
    }
  }
}
