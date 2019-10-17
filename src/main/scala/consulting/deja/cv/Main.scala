package consulting.deja.cv

import consulting.deja.cv.io.{IO, IOApp}
import consulting.deja.cv.variant.Variant

import scala.language.postfixOps

object Main extends IOApp {
  override def main[A<:IO[A]](io:A):A =
    Variant.all.foldLeft(io)((i,variant) => variant.generateForEligibleLanguages("target/")(i))
}
