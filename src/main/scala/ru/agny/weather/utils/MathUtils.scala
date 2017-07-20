package ru.agny.weather.utils

object MathUtils {

  def standardDeviation[T](v: Vector[T])(implicit ev: Fractional[T]): Double = {
    import ev._

    def diff(m: T, v: Vector[T], acc: T)(implicit ev: Fractional[T]): T = v match {
      case h +: t =>
        val deviation = h - m
        diff(m, t, acc + deviation * deviation)
      case _ => acc
    }

    if (v.nonEmpty) {
      val mean = v.sum / fromInt(v.size)
      val denom = fromInt(if (v.size > 30) v.size - 1 else v.size)
      math.sqrt(diff(mean, v, fromInt(0)) / denom toDouble())
    } else {
      0d
    }
  }

  def median[T](v: Vector[T])(implicit ev: Fractional[T]): Double = {
    import Ordering.Implicits._
    import ev._
    if (v.nonEmpty) {
      val (lower, upper) = v.sortWith((a, b) => a < b).splitAt(v.size / 2)
      val result = if (v.size % 2 == 0) (lower.last + upper.head) / fromInt(2) else upper.head
      result.toDouble()
    } else 0d
  }
}
