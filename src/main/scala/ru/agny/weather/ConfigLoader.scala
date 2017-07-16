package ru.agny.weather

import java.io.File
import io.Source._

object ConfigLoader {

  import org.json4s._
  import org.json4s.jackson.JsonMethods._

  private implicit val formats = DefaultFormats

  def load(apiName: String): ClientConfig = {
    val configFile = new File(getClass.getClassLoader.getResource(apiName).toURI)
    if (configFile.isFile) {
      ClientConfig.default
    } else {
      parse(fromFile(configFile).mkString).extract[ClientConfig]
    }
  }
}
