package service.catFacts

import _root_.config.{CatFactsConfig, ConfigApp}
import models.CatFactsModel
import zio._
import zio.http.{Client, Request, URL}
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec

final case class CatFactsLive(config: CatFactsConfig, client: Client) extends CatFacts {
  override def sendRequest(): Task[CatFactsModel] = {

    val url = URL.decode(config.url).toOption.get

    for {
      req  <- client.url(url).batched(Request.get("/"))
      data <- req.body.to[CatFactsModel]
    } yield data
  }
}

object CatFactsLive {
  val layer = ZLayer.fromZIO {
    for {
      client <- ZIO.service[Client]
      config <- ZIO.service[ConfigApp]
      catsConfig = config.catFacts
    } yield CatFactsLive(catsConfig, client)
  }
}
