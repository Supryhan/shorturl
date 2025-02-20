package shortner

import cats.effect.std.Supervisor
import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{ipv4, port}
import org.http4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.{Logger, LoggerFactory}
import shortner.appresources.ApplicationResources
import shortner.modules.{HttpApi, Programs, Services}

object Main extends IOApp.Simple {

    given loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
    given logger: Logger[IO] = loggerFactory.getLogger

    override def run: IO[Unit] = {
      Logger[IO].info("Application loaded & configured") >>
        Supervisor[IO].use { implicit sp =>
          ApplicationResources
            .make[IO]()
            .evalMap {
              res => {
                val aa: HttpApp[IO] = {
                  val services = Services.make[IO](res.postgres, res.counterRef)
                  val programs = Programs.make[IO](services)
                  val api = HttpApi.make[IO](services, programs)
                  val a: HttpApp[IO] = api.httpApp
                  a
                }
                IO(aa)
              }
            }
            .flatMap { (httpApp: HttpApp[IO]) => {
              val s: Resource[IO, Server] = EmberServerBuilder.default[IO]
                .withHost(ipv4"0.0.0.0")
                .withPort(port"8080")
                .withHttpApp(httpApp)
                .build
              s
            }
            }
            .useForever
        }
    }
}