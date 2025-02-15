package test.ziohttpstart

import zio._
import zio.http._
import zio.json.{DecoderOps, DeriveJsonCodec, EncoderOps, JsonCodec, JsonDecoder}

object MainApp extends ZIOAppDefault {

  final case class Status(id: String, description: String, active: Boolean)

  private object JsonParse {
    def bodyParse[A: JsonDecoder](body: String): Task[A] = ZIO
      .fromEither(body.fromJson[A])
      .mapError(err => new Exception(s"Can't decode body: $err"))
  }
  object Status {
    implicit val codec: JsonCodec[Status] = DeriveJsonCodec.gen[Status]
  }

  private object StatusRepo {
    var statuses: Map[String, Status] = Map.empty
  }

  private def getActiveById(id: String) = {
    StatusRepo.statuses.get(id).filter(_.active)
  }

  private def setStatus(status: Status): Unit = {
    StatusRepo.statuses += (status.id -> status)
  }

  private def getAllStatuses = {
    StatusRepo.statuses.values.toList
  }
  private object Exception {
    private sealed abstract class CustomException(message: String = "") extends Exception(message)

    private case class TestException(message: String) extends CustomException(message)

    val exceptionHandler: Throwable => Response = {
      case err: TestException => Response.internalServerError(s"Exception: $err")
      case err                => Response.badRequest(s"Exception: $err")
    }
  }

  val routes: Routes[Any, Nothing] =
    Routes(
      Method.GET / "api" / "status" / "get" / string("id") -> handler { (id: String, _: Request) =>
        for {
          status <- ZIO.succeed(getActiveById(id))
          response = Response.json(status.toJson)
        } yield response
      },
      Method.POST / "api" / "status" / "set" -> handler { (req: Request) =>
        for {
          body   <- req.body.asString
          status <- JsonParse.bodyParse[Status](body)
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

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val config      = Server.Config.default.binding("localhost", 8080)
    val configLayer = ZLayer.succeed(config)
    Server.serve(routes).provide(configLayer, Server.live)
  }
}
