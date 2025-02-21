package service.status

import models.StatusModel
import zio.macros.accessible
import zio.{Task, ZLayer}

import javax.sql.DataSource

@accessible
trait StatusRepo {
  def getAllStatuses: Task[List[StatusModel]]

  def getStatusById(id: String): Task[StatusModel]

  def setStatus(status: StatusModel): Task[String]
}

object StatusRepo {
  val live: ZLayer[DataSource, Nothing, StatusRepoLive] = StatusRepoLive.layer
}
