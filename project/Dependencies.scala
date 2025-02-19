import sbt.*

object Dependencies {

  object Version {
    val zio          = "2.1.9"
    val zioHttp      = "3.0.1"
    val zioSchema    = "1.6.1"
    val zioConfig    = "4.0.2"
    val sl4j         = "2.0.16"
    val zioLogging   = "2.3.1"
    val logback      = "1.5.8"
    val scalaLogging = "3.9.5"
  }

  object ZIO {
    lazy val schemaDerive = "dev.zio" %% "zio-schema-derivation" % Version.zioSchema % Test
    lazy val macros       = "dev.zio" %% "zio-macros"            % Version.zio
  }

  object LOGS {
    lazy val sl4j           = "org.slf4j"                   % "slf4j-api"          % Version.sl4j
    lazy val logback        = "ch.qos.logback"              % "logback-classic"    % Version.logback
    lazy val zioLogging     = "dev.zio"                    %% "zio-logging"        % Version.zioLogging
    lazy val zioLoggingLf4j = "dev.zio"                    %% "zio-logging-slf4j2" % Version.zioLogging
    lazy val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"      % Version.scalaLogging
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
    ZIO.macros,
    LOGS.sl4j,
    LOGS.zioLogging,
    LOGS.zioLoggingLf4j,
    LOGS.logback,
    LOGS.scalaLogging,
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
