package shortner.domain

import java.util.UUID

object locator {

  case class Locator(id: UUID, encoded: Long, originalName: String)
}
