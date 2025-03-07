import config.ConfigApp
import migration.Migrations
import service.catFacts.CatFacts
import service.user.UserRepo
import zio.http.netty.NettyConfig
import zio.http.netty.NettyConfig.LeakDetectionLevel
import zio.http.{Client, Server}
import zio.{Scope, ZIO, ZLayer}

object Layers {

  private val serverConf = ZLayer.fromZIO {
    ZIO.config[ConfigApp].map { config =>
      Server.Config.default.port(config.interface.port)
    }
  }

  private val nettyConf = ZLayer.succeed(
    NettyConfig.default
      .leakDetection(LeakDetectionLevel.DISABLED)
  )

  private val runtime = Scope.default

  private val base = ConfigApp.live >+> DBContext.live

  private lazy val server = (serverConf ++ nettyConf) >>> Server.customized

  private val client = Client.default

  val all =
    runtime >+>
      base >+>
      Migrations.live >+>
      client >+>
      server >+>
      UserRepo.live >+>
      CatFacts.live
}
