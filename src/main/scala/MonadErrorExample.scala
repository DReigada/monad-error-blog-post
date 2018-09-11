import cats.MonadError
import cats.implicits._

import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

import DummyValues._

object MonadErrorExample extends App {

  // -------

  def toJsonNull(str: String): Json =
    if (success) result else null

  def toJsonException(str: String): Json =
    if (success) result else throw ParseException("Could not parse JSON String")

  // -------

  def toJsonOpt(str: String): Option[Json] =
    if (success) Some(result) else None

  def toJsonEither(str: String): Try[Json] =
    if (success) Success(result)
    else Failure(ParseException("Could not parse JSON String"))

  // Using MonadError

  def toJson[F[_]](str: String)(implicit M: MonadError[F, Throwable]): F[Json] =
    if (success) M.pure(result)
    else M.raiseError(ParseException("Could not parse JSON String"))

  val parsedTry: Try[Json] = toJson[Try](content)

  val parsedEither: Either[Throwable, Json] =
    toJson[Either[Throwable, ?]](content)

  val parsedTry2 = for {
    content <- Try("{}")
    parsed <- toJson[Try](content)
  } yield parsed

  val parsedEither2 = for {
    content <- Left(new Throwable("Could not read file"))
    parsed <- toJson[Either[Throwable, ?]](content)
  } yield parsed
}
