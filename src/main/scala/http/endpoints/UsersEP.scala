package http.endpoints

import exception.Exceptions.{InternalDatabaseException, InternalException, StatusNotFoundException}
import models.{CatFactsModel, UserModel, UserRequest}
import service.catFacts.CatFacts
import service.catFacts.CatFactsService.sendRequest
import service.user.UserRepo
import service.user.UserService._
import zio.http.codec.HttpCodec
import zio.http.endpoint.Endpoint
import zio.http.{RoutePattern, Routes, Status}
import zio.schema.DeriveSchema.gen

import java.util.UUID

object UsersEP {

  private val getAllAPI =
    Endpoint(RoutePattern.GET / "api" / "user" / "get-all")
      .out[List[UserModel]](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val getAllRoute = getAllAPI
    .implement(_ => getAllUsers.mapError(err => InternalDatabaseException(err.getMessage)))

  private val getByIdAPI =
    Endpoint(RoutePattern.GET / "api" / "user" / "get")
      .query(HttpCodec.query[UUID]("id"))
      .out[UserModel](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)
      .outError[StatusNotFoundException](Status.NotFound)

  private val getByIdRoute =
    getByIdAPI
      .implement(id =>
        getUserById(id).mapError {
          case err: StatusNotFoundException   => Left(err)
          case err: InternalDatabaseException => Right(err)
        }
      )

  private val createUserAPI =
    Endpoint(RoutePattern.POST / "api" / "user" / "set")
      .in[UserRequest]
      .out[UUID](Status.Created)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val createUserRoute =
    createUserAPI.implement(userRequest =>
      createUser(userRequest).mapBoth(
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

  val routes: Routes[UserRepo with CatFacts, Nothing] =
    Routes(
      getAllRoute,
      getByIdRoute,
      createUserRoute,
      catFactsRoute
    )
}
