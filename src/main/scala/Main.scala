import _root_.http.endpoints.Status
import _root_.http.endpoints.Health
import zio._
import zio.config.typesafe.TypesafeConfigProvider
import http._
import zio.Console.printLine

object Main extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(
      TypesafeConfigProvider
        .fromResourcePath()
    )

  private val allRoutes = Health.routes ++ Status.routes

  private def startServer = Server.serve(allRoutes)

  private val program =
    for {
      _    <- ZIO.logInfo("Server is running")
      http <- startServer.exitCode
    } yield http

  override def run = {
    program
      .provide(Layers.all)
      .foldZIO(
        err => printLine(s"Execution failed with: $err").exitCode,
        _   => ZIO.succeed(ExitCode.success)
      )
  }
}
