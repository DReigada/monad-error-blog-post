import java.io.File

import DummyValues.{result, success, Json, ParseException}
import cats.MonadError
import cats.implicits._

import scala.io.Source
import scala.util.Try

// The implicit parameter can be received in the defining class
// in order to reduce the amount of boilerplate in every function
class JsonParser[F[_]](implicit M: MonadError[F, Throwable]) {

  def toJson(str: String): F[Json] =
    if (success) M.pure(result)
    else M.raiseError(ParseException("Could not parse JSON String"))

  def fileToJson(file: File): F[Json] = {
    for {
      content <- M.catchNonFatal(Source.fromFile(file).mkString)
      parsed <- toJson(content)
    } yield parsed
  }
}

object ClassExampleApp extends App {

  val jsonParserTry = new JsonParser[Try]()
  val jsonParserEither = new JsonParser[Either[Throwable, ?]]()

  val parsed: Try[Json] = jsonParserTry.toJson("{}")

  val parsedFile = jsonParserEither.fileToJson(new File("test.json"))
}
