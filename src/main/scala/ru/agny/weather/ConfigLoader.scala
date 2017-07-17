package ru.agny.weather

import java.io.File
import io.Source._

object ConfigLoader {

  import spray.json._
  import ClientConfig._

  def load(apiName: String): ClientConfig = {
    val configFile = new File(getClass.getClassLoader.getResource(apiName).toURI)
    if (configFile.isFile) {
      JsonParser(fromFile(configFile).mkString).convertTo
    } else {
      ClientConfig.default
    }
  }
}
