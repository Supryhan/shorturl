package shortner.modules

import cats.effect.{Ref, Resource, Temporal}
import shortner.services.Locators
import skunk.Session

object Services {
  def make[F[_] : Temporal](postgres: Resource[F, Session[F]],
                            counterRef: Ref[F, Long]): Services[F] = {
    val _locators = Locators.make(postgres, counterRef)
    new Services[F](_locators) {}
  }
}

sealed abstract class Services[F[_]] private(val locators: Locators[F])
