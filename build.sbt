import Dependencies.{CompilerPlugin, Libraries}
import scala.collection.immutable.Seq

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.home"
ThisBuild / organizationName := "HomeCE"

ThisBuild / evictionWarningOptions in update := EvictionWarningOptions.full

ThisBuild / scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked")

lazy val root = project
  .in(file("."))
  .settings(
    name := "urlshorter",
    scalacOptions ++= List("-Ymacro-annotations", "-Yrangepos", "-Wconf:cat=unused:info"),
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    resolvers ++= Resolver.sonatypeOssRepos("releases"),
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.circeRefined,
      Libraries.http4sServer,
      Libraries.http4sClient,
      Libraries.http4sDsl,
      Libraries.http4sCirce,
      Libraries.log4catsCore,
      Libraries.log4catsSlf4j,
      
//      Libraries.doobieCore,
//      Libraries.doobieHikari,
//      Libraries.postgreSQL,
      
      Libraries.skunkCore,
      Libraries.skunkCirce,
      Libraries.natchezCore,
      
      Libraries.fs2,
      
      Libraries.logback % Runtime,
      Libraries.scalameta,
    )

  )
