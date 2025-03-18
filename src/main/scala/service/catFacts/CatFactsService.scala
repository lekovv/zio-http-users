package service.catFacts
import exception.AppError
import models.CatFactsModel
import zio.ZIO

object CatFactsService {

  def sendRequest: ZIO[CatFacts, AppError, CatFactsModel] =
    for {
      service <- ZIO.service[CatFacts]
      result  <- service.sendRequest
    } yield result
}
