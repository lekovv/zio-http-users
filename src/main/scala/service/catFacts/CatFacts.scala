package service.catFacts

import models.CatFactsModel
import zio.Task
import zio.macros.accessible

@accessible
trait CatFacts {
  def sendRequest(): Task[CatFactsModel]

}

object CatFacts {
  val live = CatFactsLive.layer
}
