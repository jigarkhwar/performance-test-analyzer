import Dependencies._
import sbt.addCompilerPlugin

name := "performance-test-analyzer"

ThisBuild / scalaVersion := "2.13.0"
ThisBuild / version := "0.2.0-SNAPSHOT"
ThisBuild / organization := "ru.tinkoff"
ThisBuild / organizationName := "tinkoff"
ThisBuild / maintainer := "i.akhaltsev@tinkoff.ru"

version := "0.2.0"

scalacOptions += "-Ypartial-unification"

lazy val root = (project in file("."))
  .aggregate(web, core)

lazy val core = project.settings(name := "core", libraryDependencies ++= commonDependencies)

lazy val web = project
  .settings(
    name := "web",
    libraryDependencies ++= commonDependencies ++ Seq(
      akkaHttp,
      akkaActor,
      akkaStream
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0"),
    scalacOptions := compilerOptions
  )
  .dependsOn(core)
  .enablePlugins(JavaAppPackaging)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-Ymacro-annotations",
  "-encoding",
  "utf8"
)

lazy val commonDependencies = Seq(
  logback,
  scalaLogging,
  slf4j,
  typesafeConfig,
  pureConfig,
  cats,
  catsEffect,
  circeCore,
  circeGeneric,
  circeParser,
  circeGenericExtras,
  circeDerivation,
  circeDerivationAnnotaitions,
  scalatest      % "test",
  scalacheck     % "test",
  akkaStreamTest % "test",
  akkaHttpTest   % "test"
)
