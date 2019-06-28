package ru.tinkoff.pta
import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.effect._
import io.circe.parser._
import com.typesafe.config.ConfigFactory
import pureconfig._
import pureconfig.generic.auto._
import ru.tinkoff.pta.entities.{Entities, Project, Scenario, TestRun, User}
import akka.http.scaladsl.server.Directives._
import ru.tinkoff.pta.http.ResourceHttp

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

class PTAService[F[_]] extends Service {

  val name: String = "performance-test-analyzer"

  def runServerAndSystem(config: AppConfig, route: Route)(implicit system: ActorSystem, basic: ContextShift[IO]): IO[Unit] =
    for {

      binding <- IO.fromFuture(IO {
                  implicit val mat: ActorMaterializer = ActorMaterializer()
                  Http().bindAndHandle(route, config.schema.host, config.schema.port)
                })
      res <- IO(println(binding))

    } yield res

  val blocking: Resource[IO, ExecutionContext] =
    Resource
      .make(IO(Executors.newCachedThreadPool()))(exec => IO(exec.shutdown()))
      .map(ExecutionContext.fromExecutor)

  override def appConfig: AppConfig = loadConfigOrThrow[AppConfig](ConfigFactory.load(this.getClass.getClassLoader), "pta")

  override def logger(implicit system: ActorSystem): IO[LoggingAdapter] = IO.pure(Logging(system, getClass))

  def run(implicit contextShift: ContextShift[IO], timer: Timer[IO]): IO[ExitCode] = blocking.use { blockingCs =>
    for {
      userStore                   <- Entities.fromResource[User]("/pta/users.json", blockingCs)
      scenarioStore               <- Entities.fromResource[Scenario]("/pta/scenarios.json", blockingCs)
      testRunStore                <- Entities.fromResource[TestRun]("/pta/testruns.json", blockingCs)
      projectStore                <- Entities.fromResource[Project]("/pta/projects.json", blockingCs)
      userRoute                   = ResourceHttp.route("user", userStore)
      scenarioRoute               = ResourceHttp.route("scenario", scenarioStore)
      testRunRoute                = ResourceHttp.route("testrun", testRunStore)
      projectRoute                = ResourceHttp.route("project", projectStore)
      implicit0(sys: ActorSystem) <- system
      _                           <- logger
      _                           <- runServerAndSystem(appConfig, routes(name) ~ userRoute ~ scenarioRoute ~ projectRoute ~ testRunRoute)
      _                           <- IO.never
    } yield ExitCode.Success
  }

}
