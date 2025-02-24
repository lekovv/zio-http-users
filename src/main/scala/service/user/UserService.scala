package service.user

import models.{UserModel, UserRequest}
import zio.{RIO, ZIO}

import java.util.UUID

object UserService {

  def getAllUsers: RIO[UserRepo, List[UserModel]] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.getAllUsers
    } yield result

  def getUserById(id: UUID): RIO[UserRepo, UserModel] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.getUserById(id)
    } yield result

  def createUser(userRequest: UserRequest): RIO[UserRepo, UUID] =
    for {
      service <- ZIO.service[UserRepo]
      result  <- service.createUser(userRequest)
    } yield result
}
