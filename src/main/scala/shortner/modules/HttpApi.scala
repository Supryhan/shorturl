package shortner.modules

import cats.data.{Kleisli, OptionT}
import cats.effect.Async
import cats.implicits.toSemigroupKOps
import org.http4s.server.Router
import org.http4s.server.middleware.*
import org.http4s.{HttpApp, HttpRoutes, Request, Response}
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import shortner.http.routes.{LocatorRoutes, WelcomeRoutes, version}

import scala.concurrent.duration.*

object HttpApi:
  def make[F[_] : Async](services: Services[F],
                         programs: Programs[F]): HttpApi[F] =
    new HttpApi[F](services, programs) {}
end HttpApi

sealed abstract class HttpApi[F[_] : Async] private(services: Services[F],
                                                    programs: Programs[F]):
  given loggerFactory: LoggerFactory[F] = Slf4jFactory.create

  private val locatorRoutes = LocatorRoutes[F](services.locators).routes
  private val welcomeRoutes = WelcomeRoutes[F]().routes

  private val openRoutes: HttpRoutes[F] = locatorRoutes <+> welcomeRoutes

  private val routes: HttpRoutes[F] = Router(
    version.v1 -> openRoutes
  )

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = { (http: HttpRoutes[F]) =>
    AutoSlash(http)
  } andThen { (http: HttpRoutes[F]) =>
    Timeout(60.seconds)(http)
  }

  private val loggers: HttpApp[F] => HttpApp[F] = {
    (http: HttpApp[F]) =>
      RequestLogger.httpApp(true, true)(http)
  } andThen {
    (http: HttpApp[F]) =>
      ResponseLogger.httpApp(true, true)(http)
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)

end HttpApi
