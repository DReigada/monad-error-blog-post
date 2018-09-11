import DummyValues._
import cats.MonadError
import cats.implicits._

import scala.util.Try

class ErrorExamples {

  trait UIError[A] {
    def errorFromString(str: String): A

    def errorFromThrowable(thr: Throwable): A
  }

  implicit val throwableInstance: UIError[Throwable] = new UIError[Throwable] {
    override def errorFromString(str: String): Throwable = new Throwable(str)
    override def errorFromThrowable(thr: Throwable): Throwable = thr
  }

  implicit val stringInstance: UIError[String] = new UIError[String] {
    override def errorFromString(str: String): String = str
    override def errorFromThrowable(thr: Throwable): String = throwableToString(thr)
  }

  private def throwableToString(thr: Throwable): String = {
    s"""${thr.getMessage}: ${thr.getCause}
       |${thr.getStackTrace}
     """.stripMargin
  }

  // Usage

  def toJson[F[_], E](str: String)(implicit M: MonadError[F, E], E: UIError[E]): F[Json] =
    if (success) M.pure(result)
    else M.raiseError(E.errorFromString("Could not parse JSON String"))

  val parsedTry: Try[Json] = toJson[Try, Throwable](content)

  val parsedEither: Either[String, Json] = toJson[Either[String, ?], String](content)
}
