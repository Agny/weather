package ru.agny.weather

import ru.agny.weather.UserType._

case class Request(loc: Location, from: DateStamp, to: DateStamp)
