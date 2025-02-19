package shortner.http.routes

import cats.Monad
import cats.effect.Concurrent
import cats.syntax.all.*
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io.*
import org.http4s.server.Router
import shortner.domain.locator.Locator
import shortner.services.Locators

final case class LocatorRoutes[F[_] : Monad : Concurrent](locators: Locators[F]) extends Http4sDsl[F] {

  case class Original(name: String)

  case class Encoded(name: String)

  given decoderOriginal: EntityDecoder[F, Original] = jsonOf[F, Original]

  given decoderEncoded: EntityDecoder[F, Encoded] = jsonOf[F, Encoded]

  private[routes] val prefixPath = "/api"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "hello" =>
      for {
        resp <- Ok("Route & Postgres say: Hello Vitalii!")
      } yield resp

    case req@POST -> Root / "encode" => {
      val e: F[Response[F]] = for {
        orig <- req.as[Original]
        l <- locators.create(orig.name)
        resp <- Ok(s"Processed Original. Generated: ${l.shortName}")
      } yield resp
      e
    }

    case req@POST -> Root / "decode" =>
      for {
        orig <- req.as[Encoded]
        locatorOption <- locators.findBy(orig.name)
        resp <- locatorOption match {
          case Some(locator) => Ok(s"Processed Encoded. Result: ${locator.originalName}")
          case None => NotFound("Locator not found.")
        }
      } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
