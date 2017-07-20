import sbt._
import Keys._

object MainBuild extends Build {

  lazy val commonSettings = Seq(
    organization := "ru.agny",
    version := "0.1.0",
    scalaVersion := "2.12.2"
  )
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1" % "test" exclude("org.scala-lang", "scala-reflect") exclude("org.scala-lang.modules", "scala-xml_2.12")
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.9"
  lazy val akkaCors = "ch.megard" %% "akka-http-cors" % "0.2.1"
  lazy val akkaSpray = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9"

  lazy val deps = Seq(scalatest, akkaHttp, akkaCors, akkaSpray)

  lazy val root = project.in(file(".")).settings(commonSettings: _*).settings(libraryDependencies ++= deps)
}