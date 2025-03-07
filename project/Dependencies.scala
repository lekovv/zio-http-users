import sbt.*

object Dependencies {

  object Version {
    val scala        = "2.13.10"
    val zio          = "2.1.9"
    val zioHttp      = "3.0.1"
    val zioSchema    = "1.6.1"
    val zioJson      = "0.6.2"
    val zioConfig    = "4.0.2"
    val sl4j         = "2.0.16"
    val zioLogging   = "2.3.1"
    val logback      = "1.5.8"
    val scalaLogging = "3.9.5"
    val quill        = "4.8.3"
    val postgre      = "42.7.3"
    val flyway       = "9.16.0"
  }

  object ZIO {
    lazy val schemaDerive = "dev.zio" %% "zio-schema-derivation" % Version.zioSchema % Test
    lazy val schemaJson   = "dev.zio" %% "zio-schema-json"       % Version.zioSchema
    lazy val json         = "dev.zio" %% "zio-json"              % Version.zioJson
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

  object STORAGE {
    lazy val quill   = "io.getquill"   %% "quill-jdbc-zio" % Version.quill
    lazy val postgre = "org.postgresql" % "postgresql"     % Version.postgre
    lazy val flyway  = "org.flywaydb"   % "flyway-core"    % Version.flyway
  }

  object TEST {
    val zioTest         = "dev.zio" %% "zio-test"          % Version.zio % Test
    val zioTestSBT      = "dev.zio" %% "zio-test-sbt"      % Version.zio % Test
    val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % Version.zio % Test
  }

  lazy val globalProjectDependencies = Seq(
    ZIO.schemaDerive,
    ZIO.schemaJson,
    ZIO.json,
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
    STORAGE.quill,
    STORAGE.postgre,
    STORAGE.flyway,
    TEST.zioTest,
    TEST.zioTestMagnolia,
    TEST.zioTestSBT
  )
}
