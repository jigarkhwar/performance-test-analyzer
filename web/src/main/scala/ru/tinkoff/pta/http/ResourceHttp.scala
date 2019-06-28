package ru.tinkoff.pta.http

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.{ContextShift, IO, Timer}
import io.circe.{Decoder, Encoder}
import ru.tinkoff.pta.entities.Entities

import scala.util.{Failure, Success}

object ResourceHttp {

  import ru.tinkoff.pta.entities.Key

  def route[A: Encoder: Decoder: Key](prefix: String, entities: Entities[A])(implicit timer: Timer[IO],
                                                                             cs: ContextShift[IO]): Route =
    pathPrefix(prefix)(
      get {
        path(LongNumber) { id =>
          complete(entities.byId(id))
        } ~
          path("all")(complete(entities.all))
      }
        ~ post {
          entity(as[A]) { entity =>
            extractUri { uri =>
              onComplete(
                entities
                  .add(entity)
                  .unsafeToFuture()) {
                case Success(newId) =>
                  respondWithHeader(Location(Uri.apply(uri.toString() + newId))) {
                    complete(StatusCodes.Created -> "")
                  }
                case Failure(exception) => complete(StatusCodes.InternalServerError -> exception)
              }
            }
          }
        }
        ~ put {
          path(LongNumber) { id =>
            entity(as[A]) { entity =>
              onSuccess(entities.update(id, entity).unsafeToFuture()) {
                case Right(_)        => complete(StatusCodes.OK                  -> "")
                case Left(exception) => complete(StatusCodes.NotFound -> exception.getMessage)
              }

            }
          }
        }
        ~ delete {
          path(LongNumber) { id =>
            complete(entities.delete(id))
          }
        }
    )

}
