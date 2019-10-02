package consulting.deja.cv.variant

import consulting.deja.cv.io.PDFRenderable
import consulting.deja.cv.variant.rendering.StandardOverviewHTML

import scala.language.postfixOps

/** Denotes a document variant, i.e. a certain design and layout variant of the CV. */
sealed trait Variant {
  /** Language-independent HTML rendering of the variant. */
  def renderable:PDFRenderable

  /** Name of the variant. */
  def name:String
 }
object Variant {
  val all:Set[Variant] = Set(StandardOverview)

  /** Standard CV format. Gives a mid-level detail overview. Oriented towards printing on paper. */
  object StandardOverview extends Variant {
    def renderable:PDFRenderable = StandardOverviewHTML
    def name:String = "standardOverview"
  }
}
