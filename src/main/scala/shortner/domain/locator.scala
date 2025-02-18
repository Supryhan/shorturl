package shortner.domain

import java.util.UUID

object locator {

  case class Locator(shortName: String, originalName: String)
}
