package ru.tinkoff.pta.entities
import cats.effect.IO
import cats.effect.concurrent.Ref

final class TestRuns(ref: Ref[IO, List[String]]) {
  def allRuns: IO[List[String]]        = ref.get
  def addRun(tag: String): IO[Unit]    = ref.update(tag :: _)
  def deleteRun(tag: String): IO[Unit] = ref.update(tag :: _)
}

object TestRuns {
  def apply(initial: List[String]): IO[TestRuns] =
    for (ref <- Ref[IO].of(initial)) yield new TestRuns(ref)
}
