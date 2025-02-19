package config

import zio._
import zio.config._
import zio.config.magnolia.deriveConfig

case class Interface(
    host: String,
    port: Int
)

case class CatFactsConfig(url: String)

case class ConfigApp(
    interface: Interface,
    catFacts: CatFactsConfig
)

object ConfigApp {

  implicit val configDescriptor: Config[ConfigApp] = (
    deriveConfig[Interface].nested("interface") zip
      deriveConfig[CatFactsConfig].nested("catFactsConfig")
  )
    .to[ConfigApp]
    .mapKey(toKebabCase)

  val live: ZLayer[Any, Config.Error, ConfigApp] = ZLayer.fromZIO {
    ZIO.config[ConfigApp](configDescriptor)
  }
}
