package shortner.appresources

import cats.effect.std.Console
import cats.effect.{Ref, Resource, Temporal}
import cats.syntax.all.*
import fs2.io.net.Network
import skunk.Session
import skunk.*
import skunk.codec.text.*
import skunk.implicits.*
import natchez.Trace.Implicits.noop
import org.typelevel.log4cats.Logger
import skunk.codec.all.int8

sealed abstract class ApplicationResources[F[_]](val postgres: Resource[F, Session[F]],
                                                 val counterRef: Ref[F, Long]) {
}

object ApplicationResources {
  def make[F[_] : Temporal : Console : Logger : Network](): Resource[F, ApplicationResources[F]] = {


    def checkPostgresConnection(postgres: Resource[F, Session[F]]): F[Unit] =
      postgres.use { session =>
        session.unique(sql"select version();".query(text)).flatMap { v =>
          Logger[F].info(s"Connected to Postgres $v")
        }
      }

    //TODO: Split this config into: local, testing and prod
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

    def initializeCounter(postgres: Resource[F, Session[F]]): F[Ref[F, Long]] = {
      postgres.use { session =>
        val fetchCounterQuery = sql"SELECT counter FROM ref".query(int8)
        session.option(fetchCounterQuery).map(_.getOrElse(0L)).flatMap(Ref[F].of)
      }
    }

    mkPostgreSqlResource().evalMap { session =>
      initializeCounter(session).map { ref =>
        new ApplicationResources[F](session, ref) {}
      }
    }
  }
}
