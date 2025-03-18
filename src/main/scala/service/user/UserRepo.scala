package service.user

import exception.AppError
import exception.AppError.InternalDatabaseException
import models.{UserModel, UserRequest}
import zio.macros.accessible
import zio.{IO, URLayer}

import java.util.UUID
import javax.sql.DataSource

@accessible
trait UserRepo {
  def getAllUsers: IO[InternalDatabaseException, List[UserModel]]

  def getUserById(id: UUID): IO[AppError, UserModel]

  def createUser(userRequest: UserRequest): IO[InternalDatabaseException, UUID]

  def updateUser(user: UserModel): IO[InternalDatabaseException, UUID]

  def deleteUserById(id: UUID): IO[InternalDatabaseException, Unit]
}

object UserRepo {
  val live: URLayer[DataSource, UserRepoLive] = UserRepoLive.layer
}
