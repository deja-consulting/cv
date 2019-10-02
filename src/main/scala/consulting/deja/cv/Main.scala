package consulting.deja.cv

import consulting.deja.cv.io.{IO, IOApp}
import consulting.deja.cv.language.Language.German
import consulting.deja.cv.variant.Variant.StandardOverview

import scala.language.postfixOps

object Main extends IOApp {
  def main[A<:IO[A]](io:A):A = io renderPDF (StandardOverview renderable, "target/Matthias-Deja-CV-Overview-de", German, "target/Matthias-Deja-CV-Overview-de.pdf")
}
