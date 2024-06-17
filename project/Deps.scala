import sbt._

object Deps {

  lazy val Http4sVersion = "0.23.16"
  lazy val TapirVersion = "1.10.8"
  lazy val DoobieVersion = "1.0.0-RC4"

  lazy val doobieCore = "org.tpolecat" %% "doobie-core" % DoobieVersion
  lazy val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % DoobieVersion
  lazy val doobiePostgresCirce =
    "org.tpolecat" %% "doobie-postgres-circe" % DoobieVersion
  lazy val doobieHikari = "org.tpolecat" %% "doobie-hikari" % DoobieVersion
  lazy val doobieMunit = "org.tpolecat" %% "doobie-munit" % DoobieVersion

  lazy val munit = "org.scalameta" %% "munit" % "1.0.0-M11"
  lazy val refined = "eu.timepit" %% "refined" % "0.9.28"

  lazy val http4sBlazeServer =
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  lazy val http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.5.6"

  lazy val tapirHttp4sServer =
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion
  lazy val tapirOpenapiDocs =
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion
  lazy val tapirCirce =
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion
  lazy val tapirSwaggerUi =
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % TapirVersion
  lazy val sttpApiSpecCirceYaml =
    "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.10.0"

  lazy val libPhoneNumber =
    "com.googlecode.libphonenumber" % "libphonenumber" % "8.13.38"

  lazy val jbcrypt = "at.favre.lib" % "bcrypt" % "0.10.2"

  lazy val messagebird = "com.messagebird" % "messagebird-api" % "6.1.7"

  lazy val jwtCirce = "com.github.jwt-scala" %% "jwt-circe" % "10.0.1"

}
