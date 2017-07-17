package ru.agny.weather

import spray.json.DefaultJsonProtocol

import scala.concurrent.Future

trait DataProvider {
  def get(request: Request): Future[Either[Error, StatData]]
}

case class Error(v: String)

object Error extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(Error.apply)
}
