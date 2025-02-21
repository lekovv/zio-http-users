package service.status

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
      .provide(dsZL)

  override def getStatusById(id: String): Task[StatusModel] =
    ctx
      .run(
        statusSchema
          .filter(_.id == lift(id))
          .filter(_.active)
      )
      .map(_.head)
      .provide(dsZL)

  override def setStatus(status: StatusModel): Task[String] =
    ctx
      .run(statusSchema.insertValue(lift(status)).returning(value => value))
      .map(result => result.id)
      .provide(dsZL)
}

object StatusRepoLive {

  val layer: ZLayer[DataSource, Nothing, StatusRepoLive] = ZLayer.fromZIO {
    for {
      ds <- ZIO.service[DataSource]
    } yield StatusRepoLive(ds)
  }
}
