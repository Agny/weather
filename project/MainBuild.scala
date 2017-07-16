import sbt._
import Keys._

object MainBuild extends Build {

  lazy val commonSettings = Seq(
    organization := "ru.agny",
    version := "0.1.0",
    scalaVersion := "2.12.2"
  )
  lazy val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.5.2"
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1" % "test" exclude("org.scala-lang", "scala-reflect")
  lazy val deps = Seq(json4sJackson, scalatest)

  lazy val root = project.in(file(".")).settings(commonSettings: _*).settings(libraryDependencies ++= deps)
}