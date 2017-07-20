import sbt._
import Keys._
import sbtassembly.{AssemblyKeys, AssemblyPlugin}

object MainBuild extends Build {

  import AssemblyKeys._

  lazy val commonSettings = Seq(
    organization := "ru.agny",
    name := "weather-utils",
    version := "0.1.1",
    scalaVersion := "2.12.2"
  )
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.9"
  lazy val akkaCors = "ch.megard" %% "akka-http-cors" % "0.2.1"
  lazy val akkaSpray = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9"

  lazy val deps = Seq(akkaHttp, akkaCors, akkaSpray)

  lazy val root = project.in(file("."))
    .settings(commonSettings: _*).settings(libraryDependencies ++= deps)
    .enablePlugins(AssemblyPlugin).settings(
    mainClass in assembly := Some("ru.agny.weather.WebServer")
  )
}