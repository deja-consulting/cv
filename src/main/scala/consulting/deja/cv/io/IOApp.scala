package consulting.deja.cv.io

/** Alternative to [[App]] for functional programming. */
trait IOApp {
  def main[A<:IO[A]](io:A):A

  final def main(args:Array[String]):Unit = main[FileIO](FileIO)
}
