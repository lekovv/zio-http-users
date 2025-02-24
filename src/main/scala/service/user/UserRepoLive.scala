package service.user

import exception.Exceptions.{InternalDatabaseException, StatusNotFoundException}
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import models.{UserModel, UserRequest}
import zio.{Task, ZIO, ZLayer}

import java.util.UUID
import javax.sql.DataSource

final case class UserRepoLive(ds: DataSource) extends UserRepo {

  private val dsZL = ZLayer.succeed(ds)

  private val ctx = new PostgresZioJdbcContext(SnakeCase)

  import ctx._

  private val userSchema = quote {
    querySchema[UserModel]("public.users")
  }

  override def getAllUsers: Task[List[UserModel]] =
    ctx
      .run(userSchema)
      .mapError(err => InternalDatabaseException(err.getMessage))
      .provide(dsZL)

  override def getUserById(id: UUID): Task[UserModel] =
    ctx
      .run(
        userSchema
          .filter(_.id == lift(id))
      )
      .mapBoth(
        err => InternalDatabaseException(err.getMessage),
        _.headOption
      )
      .some
      .orElseFail(StatusNotFoundException(s"User with $id was not found"))
      .provide(dsZL)

  override def createUser(userRequest: UserRequest): Task[UUID] = {

    val id   = UUID.randomUUID()
    val user = UserModel(id, userRequest.first_name, userRequest.last_name, userRequest.is_active)
    ctx
      .run(userSchema.insertValue(lift(user)).returning(_.id))
      .mapBoth(
        err => InternalDatabaseException(err.getMessage),
        result => result
      )
      .provide(dsZL)
  }
}

object UserRepoLive {

  val layer: ZLayer[DataSource, Nothing, UserRepoLive] = ZLayer.fromZIO {
    for {
      ds <- ZIO.service[DataSource]
    } yield UserRepoLive(ds)
  }
}
