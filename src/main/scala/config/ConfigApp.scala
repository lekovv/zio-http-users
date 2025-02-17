package config

import service.StatusRepo
import zio._
import zio.config._
import zio.config.magnolia.deriveConfig
import zio.http._
import zio.http.netty.NettyConfig
import zio.http.netty.NettyConfig.LeakDetectionLevel

case class Interface(
    host: String,
    port: Int
)

case class ConfigApp(
    interface: Interface
)

object ConfigApp {

  implicit val configDescriptor: Config[ConfigApp] =
    deriveConfig[Interface]
      .nested("interface")
      .to[ConfigApp]

  private val serverConf = ZLayer.fromZIO {
    ZIO.config[ConfigApp].map { config =>
      Server.Config.default.port(config.interface.port)
    }
  }

  private val nettyConf = ZLayer.succeed(
    NettyConfig.default
      .leakDetection(LeakDetectionLevel.DISABLED)
  )

  private lazy val server = (serverConf ++ nettyConf) >>> Server.customized

  val all = server >+> StatusRepo.live

}
