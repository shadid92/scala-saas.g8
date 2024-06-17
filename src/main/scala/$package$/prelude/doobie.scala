package $package$.prelude

import scala.compiletime.summonAll
import scala.deriving.Mirror
import doobie.util.log.*

object deriveMeta {

  inline def deriveEnumMeta[T](using m: Mirror.SumOf[T]): Meta[T] = {
    val elemInstances =
      summonAll[Tuple.Map[m.MirroredElemTypes, ValueOf]].productIterator
        .asInstanceOf[Iterator[ValueOf[T]]]
        .map(_.value)

    val elemNames =
      summonAll[Tuple.Map[m.MirroredElemLabels, ValueOf]].productIterator
        .asInstanceOf[Iterator[ValueOf[String]]]
        .map(_.value)

    val mapping = (elemNames zip elemInstances).toMap

    Meta[String].imap { name =>
      mapping
        .get(name)
        .getOrElse(throw Exception(s"Invalid value `\$name`"))
    }(_.toString)
  }

  def logHandler[F[_]: Async] = new LogHandler[F] {
    import org.typelevel.log4cats.slf4j.Slf4jLogger
    import org.typelevel.log4cats.Logger as CatsLogger
    import cats.effect.unsafe.implicits.global
    override def run(logEvent: LogEvent): F[Unit] = {
      val catsLogger: CatsLogger[F] = Slf4jLogger.getLogger[F]
      val logPrefix =
        Thread.currentThread.getStackTrace
          .lift(1)
          .map(_.getMethodName)
          .getOrElse("UNKNOWN") + " :: "
      logEvent match {
        case ExecFailure(sql, args, label, exec, failure) =>
          catsLogger.error(logPrefix + s"""Failed Statement Execution:
         |
         |  \${sql.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
         |
         | arguments = [\${args.mkString(", ")}]
         |   elapsed = \${exec.toMillis.toString} ms exec (failed)
         |   failure = \${failure.getMessage}
         """.stripMargin)
        case ProcessingFailure(sql, args, label, exec, processing, failure) =>
          catsLogger.error(logPrefix + s"""Failed Resultset Processing:
         |
         |  \${sql.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
         |
         | arguments = [\${args.mkString(", ")}]
         |   elapsed = \${exec.toMillis.toString} ms exec + \${processing.toMillis.toString} ms processing (failed) (\${(exec + processing).toMillis.toString} ms total)
         |   failure = \${failure.getMessage}
         """.stripMargin)

        case Success(sql, args, label, exec, processing) =>
          catsLogger.info(logPrefix + s"""Success Resultset Processing:
         |  \${sql.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
         | arguments = [\${args.mkString(", ")}]
         |   elapsed = \${exec.toMillis.toString} ms exec + \${processing.toMillis.toString} ms 
         |   processing (failed) (\${(exec + processing).toMillis.toString} ms total)
         """.stripMargin)
      }
    }
  }
}
