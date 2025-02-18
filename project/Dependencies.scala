import sbt._

object Dependencies {

  object Ver {
    val cats = "2.13.0"
    val catsEffect = "3.6-28f8f29"
    val http4s = "1.0.0-M44"
    val circe = "0.14.9"
    val log4cats = "2.7.0"
    val logback = "1.5.16"
    val newtype = "0.4.4"
    val refined = "0.9.29"
    val organizeImports = "0.6.0"
    val scalameta = "1.0.0"
  }

  object Libraries {

    def http4s(artifact: String): ModuleID = "org.http4s" %% s"http4s-$artifact" % Ver.http4s
    def circe(artifact: String): ModuleID = "io.circe" %% s"circe-$artifact" % Ver.circe

    val cats = "org.typelevel" %% "cats-core" % Ver.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Ver.catsEffect

    val http4sServer = http4s("ember-server")
    val http4sClient = http4s("ember-client")
    val http4sDsl = http4s("dsl")
    val http4sCirce = http4s("circe")

    val circeCore = circe("core")
    val circeGeneric = circe("generic")
    val circeParser = circe("parser")
    val circeRefined = circe("refined")

    val refinedCore = "eu.timepit" %% "refined" % Ver.refined
    val refinedCats = "eu.timepit" %% "refined-cats" % Ver.refined

    val log4catsCore = "org.typelevel" %% "log4cats-core" % Ver.log4cats
    val log4catsSlf4j = "org.typelevel" %% "log4cats-slf4j" % Ver.log4cats

    val newtype = "io.estatico" %% "newtype" % Ver.newtype

    // Runtime
    val logback = "ch.qos.logback" % "logback-classic" % Ver.logback

    // Test
    val scalameta = "org.scalameta" %% "munit" % Ver.scalameta % Test

    // Scalafix rules
    val organizeImports = "com.github.liancheng" %% "organize-imports" % Ver.organizeImports
  }

  object CompilerPlugin {
  }


}
