import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import ru.tinkoff.pta.Routes.FileUpload

class ptaWebTests extends WordSpec with Matchers with ScalatestRouteTest with FileUpload {

  def pathBracer(path: String): String = {
    path match {
      case "" => s", Path:[/];"
      case _  => s", Path:[$path];"
    }
  }

  val testRootRoute: Route = pathSingleSlash(getFromResource("static/index.html"))

  "The service" should {

    s"return a code 200 OK on get request MAIN PAGE" + pathBracer("static/index.html") in {
      Get() ~> testRootRoute ~> check {
        status.toString() shouldEqual "200 OK"
        status shouldEqual StatusCodes.OK
      }
    }

    "return a MethodNotAllowed error for PUT requests to the MAIN PAGE" + pathBracer("static/index.html") in {
      Put() ~> Route.seal(testRootRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
      }
    }

  }

}
