package ru.tinkoff.pta

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor

trait Service {

  val name: String

  implicit val system: ActorSystem
  implicit val materializer: Materializer

  implicit val executor: ExecutionContextExecutor

  def config: Config

  val logger: LoggingAdapter

  val routes = path("")(getFromResource("static/index.html"))

}
