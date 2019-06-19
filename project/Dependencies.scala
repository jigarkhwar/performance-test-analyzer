import sbt._

object Dependencies {
  val logbackV        = "1.2.3"
  val scalaLoggingV   = "3.9.2"
  val slf4jV          = "1.7.26"
  val typesafeConfigV = "1.3.3"
  val akkaV           = "2.5.21"
  val akkaHttpV       = "10.1.7"
  val scalatestV      = "3.0.5"
  val scalacheckV     = "1.14.0"
  val catsV           = "1.6.0"
  val catsEffectV     = "1.2.0"
  val doobieV         = "0.6.0"
  val circeVersion    = "0.10.0"
  val pureConfigV     = "0.10.2"

  val logback        = "ch.qos.logback"             % "logback-classic"       % logbackV
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"        % scalaLoggingV
  val slf4j          = "org.slf4j"                  % "slf4j-api"             % slf4jV
  val typesafeConfig = "com.typesafe"               % "config"                % typesafeConfigV
  val akkaStream     = "com.typesafe.akka"          %% "akka-stream"          % akkaV
  val akkaActor      = "com.typesafe.akka"          %% "akka-actor"           % akkaV
  val akkaHttp       = "com.typesafe.akka"          %% "akka-http"            % akkaHttpV
  val scalatest      = "org.scalatest"              %% "scalatest"            % scalatestV
  val scalacheck     = "org.scalacheck"             %% "scalacheck"           % scalacheckV
  val cats           = "org.typelevel"              %% "cats-core"            % catsV
  val catsEffect     = "org.typelevel"              %% "cats-effect"          % catsEffectV
  val doobie         = "org.tpolecat"               %% "doobie-core"          % doobieV
  val circeCore      = "io.circe"                   %% "circe-core"           % circeVersion
  val circeGeneric   = "io.circe"                   %% "circe-generic"        % circeVersion
  val circeParser    = "io.circe"                   %% "circe-parser"         % circeVersion
  val pureConfig     = "com.github.pureconfig"      %% "pureconfig"           % pureConfigV
  val akkaStreamTest = "com.typesafe.akka"          %% "akka-stream-testkit"  % akkaV
  val akkaHttpTest   = "com.typesafe.akka"          %% "akka-http-testkit"    % akkaHttpV
}
