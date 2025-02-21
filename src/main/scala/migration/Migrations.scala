package migration

import org.flywaydb.core.Flyway
import zio.{ZIO, ZLayer}

import javax.sql.DataSource

final case class Migrations(ds: DataSource) {

  val migrate =
    for {
      flyway <- load
      _      <- ZIO.attempt(flyway.migrate())
    } yield ()

  private lazy val load =
    ZIO.attempt {
      Flyway
        .configure()
        .dataSource(ds)
        .locations("classpath:migrations")
        .baselineOnMigrate(true)
        .baselineVersion("0")
        .load()
    }
}

object Migrations {
  val live = ZLayer.fromZIO {
    for {
      ds <- ZIO.service[DataSource]
    } yield Migrations(ds)
  }
}
