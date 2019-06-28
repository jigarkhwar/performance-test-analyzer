package ru.tinkoff.pta.entities

import io.circe.derivation.annotations.JsonCodec
import ru.tinkoff.pta.exception.{MultipleUsers, UserNotFound}

@JsonCodec
final case class User(
    id: Long,
    name: String
)

object User {
  implicit val key: Key[User] = new Key[User] {
    def key(a: User): Long                        = a.id
    def notFound(id: Long): Throwable             = UserNotFound(id)
    def multiple(id: Long): Throwable             = MultipleUsers(id)
    def updateBody(newA: User, oldId: Long): User = newA.copy(id = oldId)
    def updateId(a: User, newId: Long): User      = a.copy(id = newId)
  }
}
