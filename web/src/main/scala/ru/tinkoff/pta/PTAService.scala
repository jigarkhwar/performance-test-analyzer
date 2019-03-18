package ru.tinkoff.pta

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

object PTAService extends App with Service {

  val name: String = "performance-test-analyzer"

  override implicit val system: ActorSystem                = ActorSystem(name)
  override implicit val materializer: Materializer         = ActorMaterializer()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher

  override def config: Config         = ConfigFactory.load()
  override val logger: LoggingAdapter = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
