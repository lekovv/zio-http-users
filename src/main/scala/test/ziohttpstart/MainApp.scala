package test.ziohttpstart

import zio._
import zio.http._
import zio.json.{DeriveJsonCodec, EncoderOps, JsonCodec}
import zio.schema.{DeriveSchema, Schema}
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec

object MainApp extends ZIOAppDefault {

  final case class Status(id: String, description: String, active: Boolean)

  /* можно использовать свой кастомный парсер
  private object JsonParse {
    def bodyParse[A: JsonDecoder](body: String): Task[A] = ZIO
      .fromEither(body.fromJson[A])
      .mapError(err => new Exception(s"Can't decode body: $err"))
  }
   */

  object Status {
    implicit val codec: JsonCodec[Status] = DeriveJsonCodec.gen[Status]
    implicit val schema: Schema[Status]   = DeriveSchema.gen[Status]
  }

  private object StatusRepo {
    var statuses: Map[String, Status] = Map.empty
  }

  private def getActiveById(id: String): Option[Status] = {
    StatusRepo.statuses.get(id).filter(_.active)
  }

  private def setStatus(status: Status): Unit = {
    StatusRepo.statuses += (status.id -> status)
  }

  private def getAllStatuses: List[Status] = {
    StatusRepo.statuses.values.toList
  }
  private object Exception {
    private case class TestException(message: String) extends Exception(message)

    val exceptionHandler: Throwable => Response = {
      case err: TestException => Response.internalServerError(s"Exception: $err")
      case err                => Response.badRequest(s"Exception: $err")
    }
  }

  //TODO: сделать маршрут дополнительно в endpoint pattern. https://zio.dev/zio-http/reference/endpoint
  val routes: Routes[Any, Nothing] = {
    Routes(
      Method.GET / "api" / "status" / "get" / string("id") -> handler { (id: String, _: Request) =>
        for {
          status <- ZIO.succeed(getActiveById(id))
          response = Response.json(status.toJson)
        } yield response
      },
      Method.POST / "api" / "status" / "set" -> handler { (req: Request) =>
        for {
          status <- req.body.to[Status]
          _      <- ZIO.succeed(setStatus(status))
        } yield Response.ok
      },
      Method.GET / "api" / "status" / "get-all" -> handler { (_: Request) =>
        for {
          status <- ZIO.succeed(getAllStatuses)
          response = Response.json(status.toJson)
        } yield response
      }
    ).handleError(Exception.exceptionHandler)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val config      = Server.Config.default.binding("localhost", 8080)
    val configLayer = ZLayer.succeed(config)
    Server.serve(routes).provide(configLayer, Server.live)
  }
}

//TODO: шаг 3. почитать про сервис паттерн. https://zio.dev/reference/architecture/programming-paradigms-in-zio (весь раздел), https://zio.dev/reference/service-pattern/
