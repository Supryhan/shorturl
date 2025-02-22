package shortner.modules

import cats.effect.Temporal
import org.typelevel.log4cats.Logger

object Programs:
  def make[F[_] : Logger : Temporal](services: Services[F]) =
    new Programs[F](services) {}
end Programs

sealed abstract class Programs[F[_] : Logger : Temporal] private(val services: Services[F]):
end Programs

