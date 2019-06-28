package ru.tinkoff.pta

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes.{`application/json` => JSON}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.IO

trait Service {

  val name: String

  def appConfig: AppConfig

  def logger(implicit system: ActorSystem): IO[LoggingAdapter]

  def system: IO[ActorSystem] = IO(ActorSystem(name))

  private def static: Route =
    get {
      pathSingleSlash(getFromResource("static/index.html")) ~
        path("""(.+\..+)""".r)(file => getFromResource(s"static/$file")) ~
        pathPrefix("css")(getFromResourceDirectory("static/css")) ~
        pathPrefix("js")(getFromResourceDirectory("static/js"))
    }

  private val version = "0.2.0"

  private def versionRoute: Route = pathPrefix("version") {
    get {
      complete(HttpEntity(JSON, s"""{ "version": "$version" }"""))
    }
  }

  def routes(sysName: String): Route = logRequestResult(sysName) {
    static ~ versionRoute
  }
}
