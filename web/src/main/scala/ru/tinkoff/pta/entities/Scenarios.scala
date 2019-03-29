package ru.tinkoff.pta.entities
import cats.effect.IO
import cats.effect.concurrent.Ref

import scala.util.Try

final class Scenarios(ref: Ref[IO, Map[Long, String]]) {
  def allScenarios: IO[Map[Long, String]] = ref.get
  def addScenario(scn: String): IO[Unit]  = ref.update(m => m + (Try(m.keys.max).getOrElse(0l) -> scn))
  def getScenario(id: Long): IO[String]   = ref.get.map(_.find(_._1 == id).map(_._2).getOrElse("NotFound"))
}

object Scenarios {
  def apply(initial: Map[Long, String]): IO[Scenarios] =
    for (ref <- Ref[IO].of(initial)) yield new Scenarios(ref)
}
