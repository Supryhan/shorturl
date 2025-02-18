package shortner.modules


import cats.data.{Kleisli, OptionT}
import cats.effect.{Async, IO}
import org.http4s.server.Router
import org.http4s.server.middleware.*
import org.http4s.{Http, HttpApp, HttpRoutes, Request, Response}
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import shortner.apphttp.routes.{LocatorRoutes, version}

import scala.concurrent.duration.*

object HttpApi {
  def make[F[_] : Async](
                          services: Services[F],
                          programs: Programs[F]
                        ): HttpApi[F] =
    new HttpApi[F](services, programs) {}

}

sealed abstract class HttpApi[F[_] : Async] private(
                                                     services: Services[F],
                                                     programs: Programs[F]
                                                   ) {
  given loggerFactory: LoggerFactory[F] = Slf4jFactory.create

  private val locatorRoutes = LocatorRoutes[F](services.locators).routes

  // Combining all the apphttp routes
  private val openRoutes: HttpRoutes[F] = locatorRoutes
  //    healthRoutes <+> itemRoutes <+> brandRoutes <+>
  //      categoryRoutes <+> loginRoutes <+> userRoutes <+>
  //      logoutRoutes <+> cartRoutes <+> orderRoutes <+>
  //      checkoutRoutes

  private val routes: HttpRoutes[F] = Router(
    version.v1 -> openRoutes
  )

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    val f: HttpRoutes[F] => Kleisli[[_$7] =>> OptionT[F, _$7], Request[F], Response[F]] = { (http: HttpRoutes[F]) =>
      AutoSlash(http) }
    f
//  } andThen {
//    val f: HttpRoutes[F] => F[Http[[_$7] =>> OptionT[F, _$7], F]] = { (apphttp: HttpRoutes[F]) =>
//    {
//      val cors: F[Http[[_$7] =>> OptionT[F, _$7], F]] = CORS.policy(apphttp)
//      cors
//    }
//    }
//    f
  } andThen {
    val f: HttpRoutes[F] => Kleisli[[_$7] =>> OptionT[F, _$7], Request[F], Response[F]] = { (http: HttpRoutes[F]) =>
    Timeout(60.seconds)(http) }
    f
  }


//  import cats.Monad
//  import org.http4s.HttpRoutes
//
//  def corsMiddleware[F[_]: Monad](apphttp: HttpRoutes[F]): F[HttpRoutes[F]] =
//    CORS.policy(apphttp)
//
//  private val middleware: HttpRoutes[F] => F[HttpRoutes[F]] = apphttp =>
//    corsMiddleware(apphttp) flatMap { corsHttp =>
//      // Припускаючи, що AutoSlash та Timeout повертають HttpRoutes[F]
//      val withAutoSlash = AutoSlash(corsHttp)
//      val withTimeout = Timeout(60.seconds)(withAutoSlash)
//      Monad[F].pure(withTimeout)
//    }



  private val loggers: HttpApp[F] => HttpApp[F] = {
    (http: HttpApp[F]) =>
      RequestLogger.httpApp(true, true)(http)
  } andThen {
    (http: HttpApp[F]) =>
      ResponseLogger.httpApp(true, true)(http)
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)

}
