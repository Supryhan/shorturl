package shortner.services

import cats.effect.Concurrent
import shortner.domain.locator.Locator

trait Locators[F[_]] {

  def create(url: String): F[Locator]

  def findBy(shortName: String): F[Option[Locator]]

}

object Locators {
  def make[F[_] : Concurrent](): Locators[F] =
    new Locators[F] {

      def create(url: String): F[Locator] = ???

      def findBy(shortName: String): F[Option[Locator]] = ???
    }
}
