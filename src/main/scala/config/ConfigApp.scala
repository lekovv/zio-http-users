package config

import zio.config.magnolia.deriveConfig
import zio.config._
import zio.http._
import zio._
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
      .leakDetection(LeakDetectionLevel.DISABLED))

  lazy val server: ZLayer[Any, Throwable, Driver with Server] = (serverConf ++ nettyConf) >>> Server.customized

}
