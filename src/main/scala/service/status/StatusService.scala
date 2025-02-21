package service.status

import models.StatusModel
import zio.{RIO, ZIO}

object StatusService {

  def getAllStatuses: RIO[StatusRepo, List[StatusModel]] =
    for {
      service <- ZIO.service[StatusRepo]
      result  <- service.getAllStatuses
    } yield result

  def getStatusById(id: String): RIO[StatusRepo, StatusModel] =
    for {
      service <- ZIO.service[StatusRepo]
      result  <- service.getStatusById(id)
    } yield result

  def setStatus(status: StatusModel): RIO[StatusRepo, String] =
    for {
      service <- ZIO.service[StatusRepo]
      result  <- service.setStatus(status)
    } yield result
}
