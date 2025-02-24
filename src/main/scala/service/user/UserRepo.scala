package service.user

import models.{UserModel, UserRequest}
import zio.macros.accessible
import zio.{Task, ZLayer}

import java.util.UUID
import javax.sql.DataSource

@accessible
trait UserRepo {
  def getAllUsers: Task[List[UserModel]]

  def getUserById(id: UUID): Task[UserModel]

  def createUser(userRequest: UserRequest): Task[UUID]

  def updateUser(user: UserModel): Task[UUID]

  def deleteUserById(id: UUID): Task[Unit]
}

object UserRepo {
  val live: ZLayer[DataSource, Nothing, UserRepoLive] = UserRepoLive.layer
}
