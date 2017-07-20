package ru.agny.weather

import ru.agny.weather.utils._
import spray.json._

case class ProcessedData(private val source: UnprocessedData) {

  import ProcessedData._

  val temperatureStat = {
    val data = source.data.flatMap(_.hours.map(_.temperature))
    gatherData(data)
  }

  val humidityStat = {
    val data = source.data.flatMap(_.hours.map(_.humidity))
    gatherData(data)
  }

  val windSpeedStat = {
    val data = source.data.flatMap(_.hours.map(_.wind.speed))
    gatherData(data)
  }
}

object ProcessedData extends DefaultJsonProtocol {

  private implicit val statFormat = jsonFormat4(StatData)

  implicit object ProcessedDataFormat extends RootJsonFormat[ProcessedData] {
    def write(p: ProcessedData) = JsObject(
      Map(
        "source" -> p.source.toJson,
        "temperature" -> p.temperatureStat.toJson,
        "humidity" -> p.humidityStat.toJson,
        "windSpeed" -> p.windSpeedStat.toJson
      )
    )

    def read(json: JsValue) =
      json.asJsObject.getFields("source") match {
        case Seq(data) => ProcessedData(data.convertTo[UnprocessedData])
        case other => deserializationError("Cannot deserialize ProcessedData: invalid input. Raw input: " + other)
      }
  }

  def gatherData[T](from: Vector[T])(implicit ev: Fractional[T]): StatData = {
    import ev._
    StatData(
      MathUtils.standardDeviation(from),
      MathUtils.median(from),
      toDouble(from.min),
      toDouble(from.max)
    )
  }
}
