package ru.tinkoff.pta.Routes
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import cats.implicits._
import ru.tinkoff.pta.entities.Scenarios

import scala.concurrent.ExecutionContextExecutor

trait ScenarioRoute {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def scenarioRoute(scenarios: Scenarios): Route =
    path("scenarios") {
      get {
        complete(scenarios.allScenarios.map(_.toString()).unsafeToFuture())
      } ~
        (put & parameter("scenarios")) { scn =>
          complete(scenarios.addScenario(scn).as("Scenario Added").unsafeToFuture())
        }
    } ~ pathPrefix("scenarios" / LongNumber) { scn =>
      get {
        complete(scenarios.getScenario(scn).unsafeToFuture())
      }
    }
}
