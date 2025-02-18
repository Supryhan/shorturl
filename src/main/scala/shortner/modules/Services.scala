package shortner.modules

import cats.effect.Temporal
import shortner.services.Locators

object Services {
  def make[F[_] : Temporal](): Services[F] = {
    val _locators = Locators.make()
    new Services[F](_locators) {}
  }
}

sealed abstract class Services[F[_]] private(val locators: Locators[F])
