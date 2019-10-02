import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/** A plain string gets parsed to a series of [[TemplateExpr]], which may get resolved only at rendering time. */
sealed trait TemplateExpr
object TemplateExpr {
  final case class Const(value:String) extends TemplateExpr
  final case class Variable(value:String) extends TemplateExpr

  def apply(input:String):Seq[TemplateExpr] = {
    val result = ArrayBuffer[TemplateExpr]()
    val buffer = new StringBuilder
    var inConst = true
    @tailrec def parse(index:Int) {
      def flushBuffer() {
        if(buffer nonEmpty) {
          if(inConst) result append Const(buffer toString)
          else result append Variable(buffer toString())
          buffer clear()
        }
      }
      if(index == (input length)) flushBuffer()
      else if(inConst) input(index) match {
        case '{' if (index+1)<(input length) && input(index+1)=='{' =>
          buffer append '{'; parse(index + 2)
        case '{' => flushBuffer(); inConst = false; parse(index + 1)
        case ch => buffer append ch; parse(index + 1)
      } else input(index) match { // !inConst
        case '}' => flushBuffer(); inConst = true; parse(index + 1)
        case ch if Character isJavaIdentifierPart ch => buffer append ch; parse(index + 1)
        case unexpected => sys error s"Unexpected character in expression: $unexpected"
      }
    }
    parse(0)
    result toVector
  }
}
