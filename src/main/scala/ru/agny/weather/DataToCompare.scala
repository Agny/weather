package ru.agny.weather

import spray.json.DefaultJsonProtocol

case class DataToCompare(left: ProcessedData, right: ProcessedData)
object DataToCompare extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(DataToCompare.apply)
}
