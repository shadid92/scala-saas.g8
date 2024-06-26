package $package$.http

import cats.effect.Async
import cats.effect.Resource
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server
import doobie.util.ExecutionContexts
import doobie.hikari.HikariTransactor
import $package$.prelude.deriveMeta.logHandler

object ServerUtil {

  def serverResource[F[_]: Async](
      host: String,
      port: Int,
      httpApp: HttpApp[F]
  ): Resource[F, Server] = BlazeServerBuilder[F]
    .bindHttp(port, host)
    .withMaxConnections(10000)
    .withHttpApp(httpApp)
    .resource

  def hikariTransactor[F[_]: Async](
      host: String,
      dbName: String,
      pgUser: String,
      pgPassword: String
  ) = for {
    ec <- ExecutionContexts.fixedThreadPool[F](10)
    xa <- HikariTransactor.fromHikariConfigCustomEc[F](
      {
        val conf = com.zaxxer.hikari.HikariConfig()
        conf.setDriverClassName("org.postgresql.Driver")
        conf.setJdbcUrl(s"jdbc:postgresql://\$host/\$dbName")
        conf.setUsername(pgUser)
        conf.setPassword(pgPassword)
        conf.setMaximumPoolSize(10)
        conf
      },
      ec,
      Some(logHandler)
    )
  } yield xa

}
