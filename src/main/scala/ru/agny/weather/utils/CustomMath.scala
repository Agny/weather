package ru.agny.weather.utils

object CustomMath {
  def standardDeviation(v: Vector[Double]) = {
    if (v.nonEmpty) {
      val mean = v.sum / v.size
      val denom = if (v.size > 30) v.size - 1 else v.size
      math.sqrt(diff(mean, v, 0) / denom)
    } else {
      0d
    }

    def diff(m: Double, v: Vector[Double], acc: Double): Double = v match {
      case h +: t =>
        val deviation = h - m
        diff(m, t, acc + deviation * deviation)
      case _ => acc
    }
  }

  def median(v: Vector[Double]) = {
    if (v.nonEmpty) {
      val (lower, upper) = v.sortWith(_ < _).splitAt(v.size / 2)
      if (v.size % 2 == 0) (lower.last + upper.head) / 2
      else upper.head
    } else 0d
  }
}
