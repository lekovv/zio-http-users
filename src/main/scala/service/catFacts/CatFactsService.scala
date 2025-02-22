package service.catFacts
import models.CatFactsModel
import zio.{RIO, ZIO}

object CatFactsService {

  def sendRequest: RIO[CatFacts, CatFactsModel] =
    for {
      service <- ZIO.service[CatFacts]
      result  <- service.sendRequest
    } yield result
}
