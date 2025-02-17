import sbt.*

object Dependencies {

  object Version {
    val zio       = "2.1.9"
    val zioHttp   = "3.0.1"
    val zioSchema = "1.6.1"
    val zioConfig = "4.0.2"
  }

  object ZIO {
    lazy val schemaDerive = "dev.zio" %% "zio-schema-derivation" % Version.zioSchema % Test
  }

  object HTTP {
    lazy val zhttp = "dev.zio" %% "zio-http" % Version.zioHttp
  }

  object CONFIG {
    lazy val core     = "dev.zio" %% "zio-config"          % Version.zioConfig
    lazy val magnolia = "dev.zio" %% "zio-config-magnolia" % Version.zioConfig
    lazy val typesafe = "dev.zio" %% "zio-config-typesafe" % Version.zioConfig
    lazy val refined  = "dev.zio" %% "zio-config-refined"  % Version.zioConfig
  }

  object TEST {
    val zioTest         = "dev.zio" %% "zio-test"          % Version.zio % Test
    val zioTestSBT      = "dev.zio" %% "zio-test-sbt"      % Version.zio % Test
    val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % Version.zio % Test
  }

  lazy val globalProjectDependencies = Seq(
    ZIO.schemaDerive,
    HTTP.zhttp,
    CONFIG.core,
    CONFIG.magnolia,
    CONFIG.refined,
    CONFIG.typesafe,
    TEST.zioTest,
    TEST.zioTestMagnolia,
    TEST.zioTestSBT
  )
}
