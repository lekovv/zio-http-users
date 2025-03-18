package service.catFacts

import _root_.config.{CatFactsConfig, ConfigApp}
import exception.AppError
import exception.AppError.{BodyParsingException, RequestExecutionException}
import models.CatFactsModel
import zio._
import zio.http.{Client, Request, URL}
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec

final case class CatFactsLive(config: CatFactsConfig, client: Client) extends CatFacts {
  override def sendRequest: IO[AppError, CatFactsModel] = {

    val url = URL.decode(config.url).toOption.get

    for {
      req <- client
        .url(url)
        .batched(Request.get("/"))
        .mapError { err =>
          RequestExecutionException(err.toString)
        }
      data <- req.body
        .to[CatFactsModel]
        .mapError { err => BodyParsingException(err.getMessage) }
    } yield data
  }
}

object CatFactsLive {
  val layer: URLayer[ConfigApp with Client, CatFactsLive] = ZLayer.fromZIO {
    for {
      client <- ZIO.service[Client]
      config <- ZIO.service[ConfigApp]
      catsConfig = config.catFacts
    } yield CatFactsLive(catsConfig, client)
  }
}
