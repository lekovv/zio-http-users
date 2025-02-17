import handlers.exceptionHandler
import zio._
import zio.http._
import zio.json.{DeriveJsonCodec, EncoderOps, JsonCodec}
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

object RoutePatternApp extends ZIOAppDefault {

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

  private def getActiveById(id: String): ZIO[Any, Nothing, Option[Status]] = {
    ZIO.succeed(StatusRepo.statuses.get(id).filter(_.active))
  }

  private def setStatus(status: Status): ZIO[Any, Nothing, Unit] = {
    ZIO.succeed(StatusRepo.statuses += (status.id -> status))
  }

  private def getAllStatuses: ZIO[Any, Nothing, List[Status]] = {
    ZIO.succeed(StatusRepo.statuses.values.toList)
  }

  val routes: Routes[Any, Nothing] = {
    Routes(
      Method.GET / "api" / "status" / "get" / string("id") -> handler { (id: String, _: Request) =>
        for {
          status <- getActiveById(id)
          response = Response.json(status.toJson)
        } yield response
      },
      Method.POST / "api" / "status" / "set" -> handler { (req: Request) =>
        for {
          status <- req.body.to[Status]
          _      <- setStatus(status)
        } yield Response.ok
      },
      Method.GET / "api" / "status" / "get-all" -> handler { (_: Request) =>
        for {
          status <- getAllStatuses
          response = Response.json(status.toJson)
        } yield response
      }
    ).handleError(exceptionHandler)
  }
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val config      = Server.Config.default.binding("localhost", 8080)
    val configLayer = ZLayer.succeed(config)
    Server.serve(routes).provide(configLayer, Server.live)
  }
}

//TODO: шаг 3. почитать про сервис паттерн. https://zio.dev/reference/architecture/programming-paradigms-in-zio (весь раздел), https://zio.dev/reference/service-pattern/
