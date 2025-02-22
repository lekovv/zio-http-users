package service.status

import exception.Exceptions.{InternalDatabaseException, StatusNotFoundException}
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import models.StatusModel
import zio.{Task, ZIO, ZLayer}

import javax.sql.DataSource

final case class StatusRepoLive(ds: DataSource) extends StatusRepo {

  private val dsZL = ZLayer.succeed(ds)

  private val ctx = new PostgresZioJdbcContext(SnakeCase)

  import ctx._

  private val statusSchema = quote {
    querySchema[StatusModel]("public.statuses")
  }

  override def getAllStatuses: Task[List[StatusModel]] =
    ctx
      .run(statusSchema)
      .mapError(err => InternalDatabaseException(err.getMessage))
      .provide(dsZL)

  override def getStatusById(id: String): Task[StatusModel] =
    ctx
      .run(
        statusSchema
          .filter(_.id == lift(id))
          .filter(_.active)
      )
      .mapBoth(
        err => InternalDatabaseException(err.getMessage),
        _.headOption
      )
      .some
      .orElseFail(StatusNotFoundException(s"Status with $id was not found"))
      .provide(dsZL)

  override def setStatus(status: StatusModel): Task[String] =
    ctx
      .run(statusSchema.insertValue(lift(status)).returning(value => value))
      .mapBoth(
        err => InternalDatabaseException(err.getMessage),
        result => result.id
      )
      .provide(dsZL)
}

object StatusRepoLive {

  val layer: ZLayer[DataSource, Nothing, StatusRepoLive] = ZLayer.fromZIO {
    for {
      ds <- ZIO.service[DataSource]
    } yield StatusRepoLive(ds)
  }
}
