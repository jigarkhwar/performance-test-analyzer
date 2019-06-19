import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.model.headers.{RawHeader, `Content-Type`}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import ru.tinkoff.pta.Routes.{FileUpload, ScenarioRoute}

class ptaWebTests
  extends WordSpec
    with Matchers
    with ScalatestRouteTest
    with FileUpload
    with ScenarioRoute
{

  def pathBracer(path: String): String = {
    path match {
      case "" => s", Path:[/];"
      case _  => s", Path:[$path];"
    }
  }

  val testRootRoute: Route = pathSingleSlash(getFromResource("static/index.html"))
  val testFileUploadRoute: Route = uploadFileRoute

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

//    "return a code 200 OK when report was been submitted" + pathBracer("test/upload/file") in {
//      val request = HttpRequest(
//        uri = "/test/upload/file", method = HttpMethods.POST,
//      ).withHeaders(
//        RawHeader("Content-Length", "multipart/form-data;"))
//
//      request ~> testFileUploadRoute ~> check {
//        status should ===(StatusCodes.OK)
//      }
//    }
  }

}
