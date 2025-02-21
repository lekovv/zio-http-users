package http.endpoints

import models.{CatFactsModel, StatusModel}
import service.catFacts.CatFacts
import service.catFacts.CatFactsService.sendRequest
import service.status.StatusRepo
import service.status.StatusService._
import zio.ZNothing
import zio.http.codec.HttpCodec
import zio.http.endpoint.{AuthType, Endpoint}
import zio.http.{handler, RoutePattern, Routes}
import zio.schema.DeriveSchema.gen

object Status {

  private val endpointGetAll: Endpoint[Unit, Unit, ZNothing, List[StatusModel], AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[StatusModel]]

  private val getAllStatusesHandler =
    endpointGetAll.implementHandler(handler(getAllStatuses).orDie)

  private val endpointGetById: Endpoint[Unit, String, ZNothing, StatusModel, AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get")
      .query(HttpCodec.query[String]("id"))
      .out[StatusModel]

  private val getStatusByIdHandler =
    endpointGetById.implementHandler(handler((id: String) => getStatusById(id)).orDie)

  private val endpointSetStatus: Endpoint[Unit, StatusModel, ZNothing, String, AuthType.None] =
    Endpoint(RoutePattern.POST / "endpoint" / "status" / "set")
      .in[StatusModel]
      .out[String]

  private val setStatusHandler =
    endpointSetStatus.implementHandler(handler((status: StatusModel) => setStatus(status)).orDie)

  private val endpointCatFacts: Endpoint[Unit, Unit, ZNothing, CatFactsModel, AuthType.None] =
    Endpoint(RoutePattern.GET / "endpoint" / "cat")
      .out[CatFactsModel]

  private val catHandler =
    endpointCatFacts.implementHandler(handler(sendRequest().orDie))

  val routes: Routes[StatusRepo with CatFacts, Nothing] =
    Routes(
      getAllStatusesHandler,
      getStatusByIdHandler,
      setStatusHandler,
      catHandler
    )
}
