import _root_.http.endpoints.{Health, UsersEP}
import migration.Migrations
import service.catFacts.CatFacts
import service.user.UserRepo
import zio.Console.printLine
import zio.Runtime.setConfigProvider
import zio._
import zio.config.typesafe.TypesafeConfigProvider
import zio.http._
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j >>>
      setConfigProvider(
        TypesafeConfigProvider
          .fromResourcePath()
      )

  private val allRoutes = Health.routes ++ UsersEP.routes

  private def startServer: URIO[UserRepo with CatFacts with Server, Nothing] = Server.serve(allRoutes)

  private val program =
    for {
      _    <- ZIO.logInfo("Server is running")
      _    <- ZIO.serviceWithZIO[Migrations](_.migrate)
      http <- startServer.exitCode
    } yield http

  override def run: UIO[ExitCode] = {
    program
      .provide(Layers.all)
      .foldZIO(
        err => printLine(s"Execution failed with: $err").exitCode,
        _ => ZIO.succeed(ExitCode.success)
      )
  }
}
