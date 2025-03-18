package service.user

import exception.AppError
import exception.AppError.InternalDatabaseException
import models.{UserModel, UserRequest}
import zio.ZIO

import java.util.UUID

object UserService {

  def getAllUsers: ZIO[UserRepo, InternalDatabaseException, List[UserModel]] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.getAllUsers
    } yield result

  def getUserById(id: UUID): ZIO[UserRepo, AppError, UserModel] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.getUserById(id)
    } yield result

  def createUser(userRequest: UserRequest): ZIO[UserRepo, InternalDatabaseException, UUID] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.createUser(userRequest)
    } yield result

  def updateUser(user: UserModel): ZIO[UserRepo, InternalDatabaseException, UUID] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.updateUser(user)
    } yield result

  def deleteUserById(id: UUID): ZIO[UserRepo, InternalDatabaseException, Unit] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.deleteUserById(id)
    } yield result
}
