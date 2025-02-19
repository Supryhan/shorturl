package shortner.appresources

import cats.effect.std.Console
import cats.effect.{Resource, Temporal}
import cats.syntax.all.*
import fs2.io.net.Network
import skunk.Session
import skunk.*
import skunk.codec.text.*
import skunk.implicits.*
import natchez.Trace.Implicits.noop
import org.typelevel.log4cats.Logger

sealed abstract class ApplicationResources[F[_]](val postgres: Resource[F, Session[F]]) {
}

object ApplicationResources {
  def make[F[_] : Temporal : Console : Logger : Network](): Resource[F, ApplicationResources[F]] = {


    def checkPostgresConnection(postgres: Resource[F, Session[F]]): F[Unit] =
      postgres.use { session =>
        session.unique(sql"select version();".query(text)).flatMap { v =>
          Logger[F].info(s"Connected to Postgres $v")
        }
      }

    def mkPostgreSqlResource(): SessionPool[F] =
      Session
        .pooled[F](
          host = "localhost",
          port = 5431,
          user = "postgres",
          password = Some("password"),
          database = "mydatabase",
          max = 2
        ).evalTap(checkPostgresConnection)

    mkPostgreSqlResource()
      .map(pg => new ApplicationResources[F](pg) {})
  }
}

