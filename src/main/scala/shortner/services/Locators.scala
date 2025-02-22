package shortner.services

import cats.effect.{Concurrent, Ref, Resource}
import cats.syntax.all.*
import shortner.domain.locator.Locator
import shortner.services.LocatorSQL.{insert, selectBy}
import skunk.*
import skunk.codec.all.*
import skunk.syntax.all.*

import java.util.UUID

trait Locators[F[_]]:

  def create(url: String): F[Locator]

  def findBy(shortName: Long): F[Option[Locator]]
end Locators

object Locators:
  def make[F[_] : Concurrent](postgres: Resource[F, Session[F]],
                              counterRef: Ref[F, Long]): Locators[F] =
    new Locators[F] {

      def create(url: String): F[Locator] = {
        postgres.use { session =>
          for {
            preparedInsert <- session.prepare(insert)
            updateCounter <- session.prepare(sql"UPDATE ref SET counter = $int8".command)
            locator <- session.transaction.use { tx =>
              for {
                currentCount <- counterRef.get
                encoded = currentCount + 1L
                locatorId = UUID.randomUUID()
                locator = Locator(locatorId, encoded, url)
                _ <- preparedInsert.execute(locator)
                _ <- updateCounter.execute(encoded)
                _ <- counterRef.set(encoded)
              } yield locator
            }
          } yield locator
        }
      }

      def findBy(decodedUrl: Long): F[Option[Locator]] = {
        postgres.use { (session: Session[F]) =>
          for {
            preparedQuery <- session.prepare(selectBy)
            result <- preparedQuery.option(decodedUrl)
          } yield result
        }
      }
    }
end Locators

private object LocatorSQL:

  val locatorCodec: Codec[Locator] =
    (uuid, int8, varchar).tupled.imap {
      case (id, encoded, original_name) => Locator(id, encoded, original_name)
    } { locator => (locator.id, locator.encoded, locator.originalName) }

  val insert: Command[Locator] =
    sql"""
      INSERT INTO locators (id, encoded, original_name)
      VALUES ($locatorCodec)
     """.command

  val selectBy: Query[Long, Locator] =
    sql"""
      SELECT id, encoded, original_name FROM locators
      WHERE encoded = $int8
     """.query(locatorCodec)
end LocatorSQL
