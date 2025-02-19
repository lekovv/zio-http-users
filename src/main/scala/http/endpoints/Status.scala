package http.endpoints

import models.{CatFactsModel, StatusModel}
import service.catFacts.CatFacts
import service.catFacts.CatFactsService.sendRequest
import service.status.StatusRepo
import service.status.StatusService._
import zio.ZNothing
import zio.http.codec.HttpCodec
import zio.http.endpoint.{AuthType, Endpoint}
import zio.http.{RoutePattern, Routes, handler}

object Status {

  private val endpointGetAll: Endpoint[Unit, Unit, ZNothing, List[StatusModel], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[StatusModel]]

  private val endpointGetById: Endpoint[Unit, String, ZNothing, Option[StatusModel], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get")
      .query(HttpCodec.query[String]("id"))
      .out[Option[StatusModel]]

  private val endpointSetStatus: Endpoint[Unit, StatusModel, ZNothing, Unit, AuthType.None] =
    Endpoint(RoutePattern.POST / "endpoint" / "status" / "set")
      .in[StatusModel]
      .out[Unit]

  private val endpointCatFacts =
    Endpoint(RoutePattern.GET / "endpoint" / "cat")
      .out[CatFactsModel]

  private val getAllStatusesHandler = endpointGetAll.implementHandler(handler(getAllStatuses).orDie)
  private val getStatusByIdHandler  = endpointGetById.implementHandler(handler((id: String) => getStatusById(id)).orDie)
  private val setStatusHandler      = endpointSetStatus.implementHandler(handler((status: StatusModel) => setStatus(status)).orDie)
  private val catHandler            = endpointCatFacts.implementHandler(handler(sendRequest().orDie))

  val routes: Routes[StatusRepo with CatFacts, Nothing] = Routes(getAllStatusesHandler, getStatusByIdHandler, setStatusHandler, catHandler)
}
