package http.endpoints

import exception.Exceptions.{InternalDatabaseException, InternalException, StatusNotFoundException}
import models.{CatFactsModel, StatusModel}
import service.catFacts.CatFacts
import service.catFacts.CatFactsService.sendRequest
import service.status.StatusRepo
import service.status.StatusService._
import zio.http.codec.HttpCodec
import zio.http.endpoint.Endpoint
import zio.http.{RoutePattern, Routes, Status}
import zio.schema.DeriveSchema.gen

object StatusEP {

  private val getAllAPI =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get-all")
      .out[List[StatusModel]](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val getAllRoute = getAllAPI
    .implement(_ => getAllStatuses.mapError(err => InternalDatabaseException(err.getMessage)))

  private val getByIdAPI =
    Endpoint(RoutePattern.GET / "endpoint" / "status" / "get")
      .query(HttpCodec.query[String]("id"))
      .out[StatusModel](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)
      .outError[StatusNotFoundException](Status.NotFound)

  private val getByIdRoute =
    getByIdAPI
      .implement(id =>
        getStatusById(id).mapError {
          case err: StatusNotFoundException   => Left(err)
          case err: InternalDatabaseException => Right(err)
        }
      )

  private val setStatusAPI =
    Endpoint(RoutePattern.POST / "endpoint" / "status" / "set")
      .in[StatusModel]
      .out[String](Status.Created)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val setStatusRoute =
    setStatusAPI.implement(status =>
      setStatus(status).mapBoth(
        err => InternalDatabaseException(err.getMessage),
        id => id
      )
    )

  private val catFactsAPI =
    Endpoint(RoutePattern.GET / "endpoint" / "cat")
      .out[CatFactsModel](Status.Ok)
      .outError[InternalException](Status.InternalServerError)

  private val catFactsRoute =
    catFactsAPI.implement(_ => sendRequest.mapError(err => InternalException(err.getMessage)))

  val routes: Routes[StatusRepo with CatFacts, Nothing] =
    Routes(
      getAllRoute,
      getByIdRoute,
      setStatusRoute,
      catFactsRoute
    )
}
