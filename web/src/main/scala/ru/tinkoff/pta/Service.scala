package ru.tinkoff.pta

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import ru.tinkoff.pta.Routes.{FileUpload, ScenarioRoute}
import ru.tinkoff.pta.entities.{Scenarios, TestRuns}

import scala.concurrent.ExecutionContextExecutor

trait Service extends FileUpload with ScenarioRoute {

  val name: String

  implicit val system: ActorSystem
  implicit val materializer: Materializer
  implicit val executor: ExecutionContextExecutor

  def appConfig: AppConfig

  val logger: LoggingAdapter

  private def static: Route = pathSingleSlash(getFromResource("static/index.html"))

//  def testRunsRoute(testRuns: TestRuns): Route = ???

  def routes(sysName: String, scenarios: Scenarios, testRuns: TestRuns): Route = logRequestResult(sysName) {
    static ~ uploadFileRoute ~ scenarioRoute(scenarios)
  }
}
