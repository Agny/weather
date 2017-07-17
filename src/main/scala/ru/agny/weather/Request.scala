package ru.agny.weather

import ru.agny.weather.utils.UserType._

case class Request(apiKey: String, loc: Location, from: DateString, to: DateString)
