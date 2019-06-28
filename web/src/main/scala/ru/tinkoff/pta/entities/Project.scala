package ru.tinkoff.pta.entities

import io.circe.derivation.annotations.JsonCodec
import ru.tinkoff.pta.exception.{MultipleProducts, ProductNotFound}

@JsonCodec
final case class Project(
    id: Long,
    name: String,
    repository: String
)

object Project {
  implicit val key: Key[Project] = new Key[Project] {
    def key(a: Project): Long                           = a.id
    def updateId(a: Project): Project                   = a.copy()
    def notFound(id: Long): Throwable                   = ProductNotFound(id)
    def multiple(id: Long): Throwable                   = MultipleProducts(id)
    def updateId(a: Project, newId: Long): Project      = a.copy(id = newId)
    def updateBody(newA: Project, oldId: Long): Project = newA.copy(id = oldId)
  }
}
