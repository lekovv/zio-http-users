package service

import models.StatusModel
import zio.{Task, ULayer, ZIO, ZLayer}

final case class StatusRepoLive() extends StatusRepo {

  var statuses: Map[String, StatusModel] = Map.empty

  override def getAllStatuses: Task[List[StatusModel]] = ZIO.succeed(statuses.values.toList)

  override def getStatusById(id: String): Task[Option[StatusModel]] = ZIO.succeed(statuses.get(id).filter(_.active))

  override def setStatus(status: StatusModel): Task[Unit] = ZIO.succeed(statuses += (status.id -> status))
}

object StatusRepoLive {

  val layer: ULayer[StatusRepoLive] = ZLayer.succeed(StatusRepoLive())
}
