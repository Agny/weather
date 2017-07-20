package ru.agny.weather

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ru.agny.weather.utils.ConfigLoader
import spray.json.DefaultJsonProtocol
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.Future
import scala.io.StdIn

object WebServer extends SprayJsonSupport with DefaultJsonProtocol {

  import ClientRequest._
  import Error.format
  import UnprocessedData.format
  import DataToCompare.format

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val config = ConfigLoader.loadApp("settings.json")
  val weatherProvider: DataProvider = WorldWeatherOnlineClient

  def main(args: Array[String]) {
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
            val responses = aggregateWeatherResponses(req)
            onSuccess(responses) {
              case Right(v1) :: Right(v2) :: Nil => complete(Right(DataToCompare(ProcessedData(v1), ProcessedData(v2))))
              case xs@h :: t if xs.exists(_.isRight) => complete(xs.find(_.isRight).get.map(ProcessedData(_)))
              case m@_ => complete(Left(Error(m.toString)))
            }
          }
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(api, config.host, config.port)

    println(s"Server online at http://${config.host}:${config.port}/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  private def aggregateWeatherResponses(req: ClientRequest) = {
    val (base, toCompare) = req.toApiRequests
    val ifCompareRequested = toCompare.map(weatherProvider.get).getOrElse(Future.successful(Left(Error("Empty result"))))
    Future.sequence(List(weatherProvider.get(base), ifCompareRequested))
  }
}
