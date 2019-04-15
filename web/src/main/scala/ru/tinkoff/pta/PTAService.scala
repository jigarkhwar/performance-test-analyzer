package ru.tinkoff.pta

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import pureconfig._
import pureconfig.generic.auto._
import akka.stream.{ActorMaterializer, Materializer}
import cats.effect.{ExitCode, IO}
import com.typesafe.config.ConfigFactory
import ru.tinkoff.pta.entities.{Scenarios, TestRuns}

import scala.concurrent.ExecutionContextExecutor

class PTAService[F[_]] extends Service {

  val name: String = "performance-test-analyzer"

  override implicit val system: ActorSystem                = ActorSystem(name)
  override implicit val materializer: Materializer         = ActorMaterializer()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher

  override def appConfig: AppConfig =
    loadConfigOrThrow[AppConfig](ConfigFactory.load(this.getClass.getClassLoader), "pta")
  override val logger: LoggingAdapter = Logging(system, getClass)

  def run: IO[ExitCode] = {
    for {
      scenarios <- Scenarios(Map.empty)
      testRuns  <- TestRuns(Nil)
      appRoute  = routes(name, scenarios, testRuns)
      _ <- IO.fromFuture(IO(Http().bindAndHandle(appRoute, appConfig.schema.host, appConfig.schema.port)))
    } yield ExitCode.Success
  }
}
