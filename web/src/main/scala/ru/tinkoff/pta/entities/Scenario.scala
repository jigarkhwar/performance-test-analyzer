package ru.tinkoff.pta.entities

import io.circe.derivation.annotations.JsonCodec
import ru.tinkoff.pta.exception.{MultipleScenarios, ScenarioNotFound}

@JsonCodec
final case class Scenario(
    id: Long,
    name: String,
    creator: User, // User.id
    classPath: String,
    project: Project, // Project.id
    timePlan: Long,
    rampUp: Long
)

object Scenario {
  implicit val key: Key[Scenario] = new Key[Scenario] {
    def key(a: Scenario): Long                            = a.id
    def notFound(id: Long): Throwable                     = ScenarioNotFound(id)
    def multiple(id: Long): Throwable                     = MultipleScenarios(id)
    def updateId(a: Scenario, newId: Long): Scenario      = a.copy(id = newId)
    def updateBody(newA: Scenario, oldId: Long): Scenario = newA.copy(id = oldId)
  }
}
