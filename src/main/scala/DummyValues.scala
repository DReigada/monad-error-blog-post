object DummyValues {
  // dummy values to simplify code
  val success = true
  val result = Json()
  val content = ""

  // dummy case classes
  case class Json()

  case class ParseException(str: String) extends Throwable
}
