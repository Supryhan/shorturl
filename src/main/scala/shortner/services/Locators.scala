package shortner.services

import cats.effect.{Concurrent, Resource}
import cats.syntax.all.*
import shortner.domain.locator.Locator
import shortner.services.LocatorSQL.{insert, selectBy}
import skunk.*
import skunk.codec.all.*
import skunk.syntax.all.*

import java.util.UUID

trait Locators[F[_]] {

  def create(url: String): F[Locator]

  def findBy(shortName: String): F[Option[Locator]]

}

object Locators {
  def make[F[_] : Concurrent](postgres: Resource[F, Session[F]]): Locators[F] =
    new Locators[F] {
      //      def createExample(url: String): F[Locator] = {
      //        val locatorId: UUID = UUID.randomUUID()
      //        val example: Locator = Locator(locatorId, "short", "original")
      //        postgres.use { (session: Session[F]) => {
      //          val e: Resource[F, PreparedCommand[F, Locator]] = session.prepareR(insert)
      //          e
      //        }.use { (command: PreparedCommand[F, Locator]) =>
      //          val e: F[Locator] = {
      //            val e1: F[Completion] = command.execute(example)
      //            e1
      //          }.as(example)
      //          e
      //        }
      //        }
      //      }
      def create(url: String): F[Locator] = {
        val locatorId: UUID = UUID.randomUUID()
        val example: Locator = Locator(locatorId, s"short-$url", url)
        postgres.use { (session: Session[F]) =>
          for {
            command <- session.prepare(insert)
            rowCount <- command.execute(example)
          } yield example
        }
      }

      def findBy(shortName: String): F[Option[Locator]] = {
        postgres.use { (session: Session[F]) =>
          for {
            preparedQuery <- session.prepare(selectBy)
            result <- preparedQuery.option(shortName)
          } yield result
        }
      }
    }
}

private object LocatorSQL {

  val locatorCodec: Codec[Locator] =
    (uuid, varchar, varchar).tupled.imap {
      case (id, short_name, original_name) => Locator(id, short_name, original_name)
    } { locator => (locator.id, locator.shortName, locator.originalName) }

  val insert: Command[Locator] =
    sql"""
      INSERT INTO locators (id, short_name, original_name)
      VALUES ($locatorCodec)
     """.command

  val selectBy: Query[String, Locator] =
    sql"""
      SELECT id, short_name, original_name FROM locators
      WHERE short_name = $varchar
     """.query(locatorCodec)


}
