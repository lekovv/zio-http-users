package service.catFacts

import config.ConfigApp
import exception.AppError
import models.CatFactsModel
import zio.http.Client
import zio.macros.accessible
import zio.{IO, URLayer}

@accessible
trait CatFacts {
  def sendRequest: IO[AppError, CatFactsModel]
}

object CatFacts {
  val live: URLayer[ConfigApp with Client, CatFactsLive] = CatFactsLive.layer
}
