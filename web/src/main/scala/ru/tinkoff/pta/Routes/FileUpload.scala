package ru.tinkoff.pta.Routes
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.Framing
import akka.util.ByteString
import cats.effect.IO

import scala.concurrent._

trait FileUpload {

  type Request = Int

  case class ParseInfo(startTime: Option[String], endTime: Option[String], requests: Vector[Request]) ///TODO вынести

  def parseFile(currentState: ParseInfo, string: ByteString): IO[ParseInfo] = IO { ParseInfo(None, None, Vector.empty) } ///TODO вынести

//  def uploadFileRoute: Route = path("test" / "upload" / "file") {
//    post {
//      fileUpload("logSource") {
//        case (_, byteSource) =>
//          val result: Future[ParseInfo] =
//            byteSource
//              .via(Framing.delimiter(ByteString(System.lineSeparator), 2 * 1024, allowTruncation = true))
//              .runFoldAsync(ParseInfo(None, None, Vector.empty))((currentState, string) =>
//                parseFile(currentState, string).unsafeToFuture)
//
//          onSuccess(result)(_ => complete(s"OK"))
//      }
//    }
//  }

}
