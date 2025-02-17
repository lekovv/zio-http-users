package test.ziohttpstart

import zio._
import zio.http._
import zio.http.codec.HttpCodec
import zio.http.endpoint.{AuthType, Endpoint}
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

    def getStatusById(id: String): ZIO[Any, ZNothing, Option[Status]] = {
      ZIO.succeed(StatusRepo.statuses.get(id).filter(_.active))
    }

    def setStatus(status: Status): ZIO[Any, ZNothing, Unit] = {
      ZIO.succeed(StatusRepo.statuses += (status.id -> status))
    }
  }

  private val endpointGetAll: Endpoint[Unit, Unit, ZNothing, List[Status], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[Status]]

  private val endpointGetById: Endpoint[Unit, String, ZNothing, Option[Status], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get")
      .query(HttpCodec.query[String]("id"))
      .out[Option[Status]]

  private val endpointSetStatus: Endpoint[Unit, Status, ZNothing, Unit, AuthType.None] =
    Endpoint(RoutePattern.POST / "endpoint" / "status" / "set")
      .in[Status]
      .out[Unit]

  private val getAllStatusesRoute = endpointGetAll.implementHandler(handler(StatusRepo.getAllStatuses))
  private val getStatusById       = endpointGetById.implementHandler(handler((id: String) => StatusRepo.getStatusById(id)))
  private val setStatus           = endpointSetStatus.implementHandler(handler((status: Status) => StatusRepo.setStatus(status)))

  val routes: Routes[Any, Nothing] = Routes(getAllStatusesRoute, getStatusById, setStatus)

  override def run: ZIO[Any, Throwable, Nothing] = {
    val config      = Server.Config.default.binding("localhost", 8080)
    val configLayer = ZLayer.succeed(config)
    Server.serve(routes).provide(configLayer, Server.live)
  }
}
