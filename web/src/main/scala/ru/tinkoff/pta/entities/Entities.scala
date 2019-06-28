package ru.tinkoff.pta.entities

import java.io.{BufferedReader, InputStreamReader}

import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, IO}
import cats.implicits._
import io.circe.Decoder
import io.circe.parser._

import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._

trait Entities[A] {
  def all: IO[List[A]]

  def getById(id: Long): IO[Option[A]]

  def byId(id: Long): IO[A]

  def add(entity: A): IO[Long]

  def update(id: Long, entity: A): IO[Either[Throwable, A]]

  def delete(id: Long): IO[Unit]
}

object Entities {

  final case class State[A: Key](byIdMap: Map[Long, A])

  private final case class ListEntities[A: Key](ref: Ref[IO, State[A]]) extends Entities[A] {

    override def all: IO[List[A]] = ref.get.map(_.byIdMap.values.toList)

    override def getById(id: Long): IO[Option[A]] = ref.get.map(_.byIdMap.get(id))

    override def byId(id: Long): IO[A] = getById(id).flatMap(_.liftTo[IO](Key.notFound[A](id)))

    override def add(entity: A): IO[Long] = ref.modify { s =>
      val maxId = s.byIdMap.keys.max + 1
      (State(s.byIdMap + (maxId -> Key.updateId(entity, maxId))), maxId)
    }

    override def update(id: Long, entity: A): IO[Either[Throwable, A]] = ref.modify { s =>
      if (s.byIdMap.contains(id))
        (State(s.byIdMap + (id -> Key.updateWithoutId(entity, id))), Right(entity))
      else
        (s, Left(Key.notFound(id)))
    }

    override def delete(id: Long): IO[Unit] = ref.modify(s => (State(s.byIdMap.removed(id)), s))

  }

  private def makeListEntities[A: Key](items: List[A]): IO[ListEntities[A]] =
    items
      .groupBy(Key.key[A])
      .toList
      .traverse {
        case (id, List(a)) => IO.pure(id -> a)
        case (id, _)       => IO.raiseError(Key.multiple[A](id))
      }
      .flatMap(byId => Ref.of[IO, State[A]](State(byId.toMap)))
      .map(state => ListEntities(state))
//      .map(byId => ListEntities(State(items, byId.toMap)))

  def fromResource[A: Key: Decoder](path: String, blocking: ExecutionContext)(
      implicit basic: ContextShift[IO]): IO[Entities[A]] = {

    val read: IO[List[A]] = IO {
      val stream = getClass.getResourceAsStream(path)
      val reader = new BufferedReader(new InputStreamReader(stream))
      val total  = reader.lines().iterator().asScala.mkString
      decode[List[A]](total): Either[Throwable, List[A]]
    }.rethrow

    for {
      _      <- IO.shift(blocking)
      entity <- read
      list   <- makeListEntities(entity)
      _      <- IO.shift(basic)
    } yield list
  }

}
