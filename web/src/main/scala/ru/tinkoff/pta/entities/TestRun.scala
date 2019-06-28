package ru.tinkoff.pta.entities

import io.circe.generic.JsonCodec
import ru.tinkoff.pta.exception.{MultipleTestRuns, TestRunNotFound}

@JsonCodec
final case class TestRun(
    id: Long,
    profile: String,
    scenario: Scenario,
    status: String,
    logPath: String
)

object TestRun {
  implicit val key: Key[TestRun] = new Key[TestRun] {
    def key(a: TestRun): Long                           = a.id
    def notFound(id: Long): Throwable                   = TestRunNotFound(id)
    def multiple(id: Long): Throwable                   = MultipleTestRuns(id)
    def updateBody(newA: TestRun, oldId: Long): TestRun = newA.copy(id = oldId)
    def updateId(a: TestRun, newId: Long): TestRun      = a.copy(id = newId)
  }
}
