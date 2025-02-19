package http.endpoints

import models.StatusModel
import service.StatusRepo
import service.StatusService._
import zio.ZNothing
import zio.http.codec.HttpCodec
import zio.http.endpoint.{AuthType, Endpoint}
import zio.http.{handler, RoutePattern, Routes}

object Status {

  private val endpointGetAll: Endpoint[Unit, Unit, ZNothing, List[StatusModel], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[StatusModel]]

  private val endpointGetById: Endpoint[Unit, String, ZNothing, Option[StatusModel], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get")
      .query(HttpCodec.query[String]("id"))
      .out[Option[StatusModel]]

  private val endpointSetStatus =
    Endpoint(RoutePattern.POST / "endpoint" / "status" / "set")
      .in[StatusModel]
      .out[Unit]

  private val getAllStatusesHandler = endpointGetAll.implementHandler(handler(getAllStatuses).orDie)
  private val getStatusByIdHandler  = endpointGetById.implementHandler(handler((id: String) => getStatusById(id)).orDie)
  private val setStatusHandler      = endpointSetStatus.implementHandler(handler((status: StatusModel) => setStatus(status)).orDie)

  val routes: Routes[StatusRepo, Nothing] = Routes(getAllStatusesHandler, getStatusByIdHandler, setStatusHandler)
}
