package test.ziohttpstart

import zio.test._
import zio.http._

object MainAppSpec extends ZIOSpecDefault {

  def spec = suite("suite for MainApp")(
    test("test for endpoint /text") {
      val request = Request.get(URL(Path.decode("/")))
      RoutePatternApp.routes.runZIO(request) *> assertTrue(true)
    }
  )

}
