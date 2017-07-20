package ru.agny.weather.utils

import java.io.File

import scala.io.Source._

object ConfigLoader {

  import spray.json._
  import ClientConfig.format
  import AppConfig.format

  def loadClient(apiName: String): ClientConfig = {
    val configFile = new File(getClass.getClassLoader.getResource(apiName).toURI)
    if (configFile.isFile) {
      JsonParser(fromFile(configFile).mkString).convertTo[ClientConfig]
    } else {
      ClientConfig.default
    }
  }

  def loadApp(commons: String): AppConfig = {
    val configFile = new File(getClass.getClassLoader.getResource(commons).toURI)
    if (configFile.isFile) {
      JsonParser(fromFile(configFile).mkString).convertTo[AppConfig]
    } else {
      AppConfig.default
    }
  }
}
