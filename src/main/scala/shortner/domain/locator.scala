package shortner.domain

import java.util.UUID

object locator {

  case class Locator(id: UUID, shortName: String, originalName: String)
}
