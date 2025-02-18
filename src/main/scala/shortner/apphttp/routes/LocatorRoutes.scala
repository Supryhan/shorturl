package shortner.apphttp.routes

import cats.effect.Concurrent
import cats.Monad
import cats.syntax.all.*
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe.*
import org.http4s.dsl.io.*
import shortner.services.Locators
import io.circe.generic.auto.*


final case class LocatorRoutes[F[_] : Monad : Concurrent](locators: Locators[F]) extends Http4sDsl[F] {

  case class Original(name: String)
  case class Encoded(name: String)
  given decoderOriginal: EntityDecoder[F, Original] = jsonOf[F, Original]
  given decoderEncoded: EntityDecoder[F, Encoded] = jsonOf[F, Encoded]

  private[routes] val prefixPath = "/api"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "hello" => {
      val e: F[Response[F]] = Ok("Hello Vitalii!")
      e
    }

    case req@POST -> Root / "encode" =>
      for {
        orig <- req.as[Original]
        resp <- Ok("Processed Original.")
      } yield resp

    case req@POST -> Root / "decode" =>
      for {
        orig <- req.as[Encoded]
        resp <- Ok("Processed Encoded.")
      } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
