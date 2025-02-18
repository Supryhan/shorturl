package shortner.appresources

import cats.effect.Resource
import cats.effect.kernel.Async

sealed abstract class EmptyResources[F[_]]() {
}

object EmptyResources {
  def make[F[_] : Async](): Resource[F, EmptyResources[F]] = {
    Resource.pure[F, EmptyResources[F]](new EmptyResources[F] {})
  }
}

