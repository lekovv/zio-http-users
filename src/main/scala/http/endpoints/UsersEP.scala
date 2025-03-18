package http.endpoints

import exception.AppError
import exception.AppError.{BodyParsingException, InternalDatabaseException, RequestExecutionException, UserNotFoundException}
import models.{CatFactsModel, UserModel, UserRequest}
import service.catFacts.CatFactsService.sendRequest
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

  private val getByIdAPI =
    Endpoint(RoutePattern.GET / "api" / "user" / "get")
      .query(HttpCodec.query[UUID]("id"))
      .out[UserModel](Status.Ok)
      .outErrors[AppError](
        HttpCodec.error[InternalDatabaseException](Status.InternalServerError),
        HttpCodec.error[UserNotFoundException](Status.NotFound)
      )

  private val createUserAPI =
    Endpoint(RoutePattern.POST / "api" / "user" / "set")
      .in[UserRequest]
      .out[UUID](Status.Created)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val updateUserAPI =
    Endpoint(RoutePattern.PUT / "api" / "user" / "update")
      .in[UserModel]
      .out[UUID](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val deleteUserByIdAPI =
    Endpoint(RoutePattern.DELETE / "api" / "user" / "delete")
      .query(HttpCodec.query[UUID]("id"))
      .out[Unit](Status.Ok)
      .outError[InternalDatabaseException](Status.InternalServerError)

  private val catFactsAPI =
    Endpoint(RoutePattern.GET / "endpoint" / "cat")
      .out[CatFactsModel](Status.Ok)
      .outErrors[AppError](
        HttpCodec.error[RequestExecutionException](Status.InternalServerError),
        HttpCodec.error[BodyParsingException](Status.BadRequest)
      )

  val routes = Routes(
    getAllAPI.implement(_ => getAllUsers),
    getByIdAPI.implement(id => getUserById(id)),
    createUserAPI.implement(req => createUser(req)),
    updateUserAPI.implement(req => updateUser(req)),
    deleteUserByIdAPI.implement(id => deleteUserById(id)),
    catFactsAPI.implement(_ => sendRequest)
  )
}
