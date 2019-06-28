package ru.tinkoff.pta.exception

abstract class StacklessException(message: String) extends Exception(message, null, false, false)

final case class ProductNotFound(id: Long)  extends StacklessException(s"Product $id not found")
final case class MultipleProducts(id: Long) extends StacklessException(s"Multiple products with id $id")

final case class ScenarioNotFound(id: Long)  extends StacklessException(s"Scenario $id not found")
final case class MultipleScenarios(id: Long) extends StacklessException(s"Multiple scenarios with id $id")

final case class UserNotFound(id: Long)  extends StacklessException(s"User $id not found")
final case class MultipleUsers(id: Long) extends StacklessException(s"Multiple users with id $id")

final case class TestRunNotFound(id: Long)  extends StacklessException(s"TestRun $id not found")
final case class MultipleTestRuns(id: Long) extends StacklessException(s"Multiple TestRun with id $id")
