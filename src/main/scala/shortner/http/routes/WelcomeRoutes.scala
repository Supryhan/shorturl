package shortner.http.routes

import cats.Monad
import cats.effect.Concurrent
import cats.syntax.all.*
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io.*
import org.http4s.server.Router

final case class WelcomeRoutes[F[_] : Monad : Concurrent]() extends Http4sDsl[F] {

  private[routes] val prefixPath = "/api"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "welcome" =>
      for {
        resp <- Ok("Hello in URL shortener application!")
      } yield resp
  }
  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}