package test.ziohttpstart

import zio._
import zio.http._
import zio.http.endpoint.Endpoint
import zio.schema.{DeriveSchema, Schema}

object EndpointPatternApp extends ZIOAppDefault {

  case class Status(id: String, description: String, active: Boolean)

  object Status {
    implicit val schema: Schema[Status] = DeriveSchema.gen
  }

  object StatusRepo {
    var statuses: Map[String, Status] = Map.empty

    def getAllStatuses: ZIO[Any, ZNothing, List[Status]] = {
      ZIO.succeed(StatusRepo.statuses.values.toList)
    }
  }

  private val endpoint =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[Status]]

  private val getAllStatusesHandler = handler(StatusRepo.getAllStatuses)
  val routes: Routes[Any, Nothing]  = endpoint.implementHandler(getAllStatusesHandler).toRoutes @@ Middleware.debug

  override def run: ZIO[Any, Throwable, Nothing] = {
    val config = Server.Config.default.binding("localhost", 8080)
    val configLayer = ZLayer.succeed(config)
    Server.serve(routes).provide(configLayer, Server.live)
  }
}
