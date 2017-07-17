package ru.agny.weather.utils

import java.io.File

import scala.io.Source._

object ConfigLoader {

  import spray.json._
  import ClientConfig.format

  def load(apiName: String): ClientConfig = {
    val configFile = new File(getClass.getClassLoader.getResource(apiName).toURI)
    if (configFile.isFile) {
      JsonParser(fromFile(configFile).mkString).convertTo
    } else {
      ClientConfig.default
    }
  }
}
