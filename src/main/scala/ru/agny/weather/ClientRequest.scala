package ru.agny.weather

import ru.agny.weather.utils.DateFormatter
import ru.agny.weather.utils.UserType.{Location, DateString}
import spray.json.{JsString, JsValue, RootJsonFormat, DefaultJsonProtocol}

case class ClientRequest(apiKey: String, base: RequestData, compareWith: Option[RequestData], periodType: PeriodType) {

  import DateFormatter._

  def toApiRequests: (ApiRequest, Option[ApiRequest]) = {
    implicit val format = createFormatter
    (ApiRequest(apiKey, base.city, base.startDate, base.endDate),
      compareWith.map(x => ApiRequest(apiKey, x.city, x.startDate, x.endDate)))
  }
}

case class RequestData(city: Location, startDate: DateString, endDate: DateString)

sealed trait PeriodType
object PeriodType {
  def fromString(v: String): PeriodType = {
    Vector(Whole, DayTime, NightTime).find(_.toString == v) getOrElse Whole
  }
}
case object Whole extends PeriodType
case object DayTime extends PeriodType
case object NightTime extends PeriodType

object ClientRequest extends DefaultJsonProtocol {
  implicit object PeriodTypeFormat extends RootJsonFormat[PeriodType] {
    def write(p: PeriodType) = JsString(p.toString)

    def read(value: JsValue) = PeriodType.fromString(value.toString())
  }
  private implicit val dataFormat = jsonFormat3(RequestData)

  implicit val format = jsonFormat4(ClientRequest.apply)
}
