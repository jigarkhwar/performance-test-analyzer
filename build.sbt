import Dependencies._

name := "performance-test-analyzer"

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "ru.tinkoff"
ThisBuild / organizationName := "tinkoff"

version := "0.1"

scalacOptions += "-Ypartial-unification"

lazy val root = (project in file("."))
  .aggregate(web, core)

lazy val core = project.settings(name := "core", libraryDependencies ++= commonDependencies)

lazy val web = project
  .settings(name := "web",
            libraryDependencies ++= commonDependencies ++ Seq(
              akkaHttp,
              akkaActor,
              akkaStream
            ))
  .dependsOn(core)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
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
  scalatest  % "test",
  scalacheck % "test"
)
