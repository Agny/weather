package ru.agny.weather

import ru.agny.weather.UserType.{Degree, Kmph}

case class Wind(speed: Kmph, direction: Degree)
