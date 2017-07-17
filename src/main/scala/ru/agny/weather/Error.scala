package ru.agny.weather

import spray.json.DefaultJsonProtocol

case class Error(v: String)

object Error extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(Error.apply)
}
