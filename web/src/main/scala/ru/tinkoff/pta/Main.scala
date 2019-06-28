package ru.tinkoff.pta
import cats.effect._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = new PTAService[IO].run

}
