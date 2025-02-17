package service

import models.StatusModel
import zio.macros.accessible
import zio.{Task, ULayer}

@accessible
trait StatusRepo {
  def getAllStatuses: Task[List[StatusModel]]

  def getStatusById(id: String): Task[Option[StatusModel]]

  def setStatus(status: StatusModel): Task[Unit]
}

object StatusRepo {
  val live: ULayer[StatusRepoLive] = StatusRepoLive.layer
}
