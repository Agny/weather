package ru.agny.weather

import ru.agny.weather.UserType._

case class Request(apiKey: String, loc: Location, from: DateStamp, to: DateStamp)
