package ru.agny.weather

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.Future
import scala.io.StdIn

object WebServer extends SprayJsonSupport with DefaultJsonProtocol {

  import ClientRequest._
  import Error.format
  import UnprocessedData.format
  import DataToCompare.format

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val weatherProvider: DataProvider = WorldWeatherOnlineClient

    val api = cors() {
      path("api" / "raw") {
        post {
          entity(as[ClientRequest]) { req => {
            val (base, _) = req.toApiRequests
            onSuccess(weatherProvider.get(base)) { result =>
              complete(result)
            }
          }
          }
        }
      }
    } ~ cors() {
      path("api" / "processed") {
        post {
          entity(as[ClientRequest]) { req => {
            val (base, toCompare) = req.toApiRequests
            val responses = Future.sequence(List(weatherProvider.get(base), toCompare.map(weatherProvider.get).getOrElse(Future.successful(Left(Error("Empty result"))))))
            val result = responses.map {
              case Right(v1) :: Right(v2) :: Nil => Right(DataToCompare(ProcessedData(v1), ProcessedData(v2)))
              case xs@List(_) if xs.exists(_.isRight) => xs.find(_.isRight).get.map(ProcessedData(_))
              case _ => Left(Error("Nothing to show"))
            }
            onSuccess(result) { result =>
              complete(result)
            }
          }
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(api, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
