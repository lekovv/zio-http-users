import config._
import zio._
import zio.config.typesafe.TypesafeConfigProvider
import http._
import http_*.Endpoints.endRoutes


object EndpointPatternApp extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(
      TypesafeConfigProvider
        .fromResourcePath()
    )

  override def run = {
    Server.serve(endRoutes).provide(ConfigApp.all)
  }
}
