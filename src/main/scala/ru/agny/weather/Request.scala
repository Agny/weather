package ru.agny.weather

import ru.agny.weather.UnitType._

case class Request(loc: Location, from: DateStamp, to: DateStamp)
