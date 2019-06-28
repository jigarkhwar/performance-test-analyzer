package ru.tinkoff.pta

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes.{`application/json` => JSON}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import cats.Monoid
import cats.effect._
import cats.implicits._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.concurrent.Future
import scala.concurrent.duration._

package object http {

  implicit def fromEntityUnmarshaller[A: Decoder]: FromEntityUnmarshaller[A] =
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(JSON)
      .flatMap { implicit ctx => _ => bs =>
        decode[A](bs.utf8String).fold(Future.failed, Future.successful)
      }

  implicit def toResponseMarshaller[A: Encoder](implicit cs: ContextShift[IO], timer: Timer[IO]): ToEntityMarshaller[IO[A]] =
    Marshaller.withFixedContentType(JSON) { ia: IO[A] =>
      val iaWithTimeout =
        IO.race(
            timer.sleep(60.seconds),
            ia.map(a => ByteString(a.asJson.noSpaces)),
          )
          .flatMap {
            case Right(s) => s.pure[IO]
            case Left(()) => IO.raiseError(TimeoutError())
          }

      HttpEntity(
        JSON,
        Source.fromFuture(iaWithTimeout.unsafeToFuture())
      )

    }

  implicit val ioRouteMonoid: Monoid[IO[Route]] = new Monoid[IO[Route]] {
    def empty: IO[Route]                               = IO.pure(reject)
    def combine(x: IO[Route], y: IO[Route]): IO[Route] = (x, y).mapN(_ ~ _)
  }

  final case class TimeoutError() extends Throwable
}
