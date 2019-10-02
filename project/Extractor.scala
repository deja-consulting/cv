import TemplateParser._

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.xml._

/** Used by [[TemplateParser]] to extract templates and translations alike. */
trait Extractor[A] {
  def apply(extractElem:Elem, startState:A):A = Extractor extract (extractElem, this, startState)

  def isExtractChild(extractElem:Elem):Boolean
  def maybeExtractTemplate(templateElem:Elem, extractElem:Elem, state:A):Option[A]
  def result(extractElem:Elem, state:A, instructions:Vector[String]):A
}
object Extractor {

  private def extract[A](extractElem:Elem, extractor:Extractor[A], state:A):A = {
    var result:A = state
    val instructions:ArrayBuffer[String] = ArrayBuffer()
    var pendingCloseInstructions:List[String] = Nil
    var pendingContinuations:List[Traversable[Node]] = Nil
    @tailrec def traverse(nodes:Traversable[Node]) {
      nodes headOption match {
        case None =>
          if(pendingCloseInstructions nonEmpty) {
            instructions append (pendingCloseInstructions head)
            pendingCloseInstructions = pendingCloseInstructions tail
          }
          if(pendingContinuations nonEmpty) {
            val continuation = pendingContinuations.head
            pendingContinuations = pendingContinuations.tail
            traverse(continuation)
          }
        case Some(node) => node match {
          case _:Comment => traverse(nodes tail)
          case elem:Elem =>
            extractor maybeExtractTemplate (elem, extractElem, result) match {
              case Some(extractedTemplate) =>
                result = extractedTemplate
                traverse(nodes tail)
              case None =>
                if (isAutoCloseTagElem(elem)) {
                  instructions append appendAutoCloseTagInstruction(elem)
                  traverse(nodes tail)
                }
                else {
                  instructions append appendTagOpenInstruction(elem)
                  pendingCloseInstructions = appendTagCloseInstruction(elem) :: pendingCloseInstructions
                  pendingContinuations = (nodes tail) :: pendingContinuations
                  traverse(elem child)
                }
            }
          case ref:EntityRef => instructions append appendTextInstruction(ref text); traverse(nodes tail)
          case Group(contents) => traverse(contents ++ (nodes tail))
          case PCData(string) => instructions append appendTextInstruction(string); traverse(nodes tail)
          case _:ProcInstr => traverse(nodes tail)
          case Text(text) => instructions append appendTextInstruction(text); traverse(nodes tail)
          case unparsed:Unparsed => sys error s"Unparsed element: $unparsed"
          case unknownNode => sys error s"Unknown node type: $unknownNode"
        }
      }
    }
    if(extractor isExtractChild extractElem) traverse(extractElem child)
    else traverse(Seq(extractElem))
    extractor result (extractElem, result, instructions toVector)
  }

  @tailrec private def appendAttributeList(attrs:Seq[(String,String)], attrList:String=""):String = attrs match {
    case Seq() => attrList
    case Seq((k,_), rest@_*) if k startsWith AttributeKeyPrefix => appendAttributeList(rest, attrList)
    case Seq((k,v), rest@_*) => appendAttributeList(rest, s"""$attrList, "${escapeScalaString(k)}" -> ${instructionsForExprInAttributeValue(TemplateExpr(v))}""")
  }

  private def appendAutoCloseTagInstruction(elem:Elem):String = s"""a = a.tagAutoClose("${escapeScalaString(getFlatLabel(elem))}"${appendAttributeList(elem.attributes.asAttrMap.toSeq)});"""
  private def appendTagCloseInstruction(elem:Elem):String = s"""a = a.tagClose("${escapeScalaString(getFlatLabel(elem))}");"""
  private def appendTagOpenInstruction(elem:Elem):String = s"""a = a.tagOpen("${escapeScalaString(getFlatLabel(elem))}"${appendAttributeList(elem.attributes.asAttrMap.toSeq)});"""
  private def appendTextInstruction(text:String):String = s"""a = a${instructionsForExprInText(TemplateExpr(text))};"""

  private def escapeScalaString(str:String):String = str
    .replaceAllLiterally("\\", "\\\\").replaceAllLiterally("\"", "\\\"").replaceAllLiterally("\n", "\\n")
    .replaceAllLiterally("\r", "\\r").replaceAllLiterally("\t", "\\t")

  private def getFlatLabel(elem:Elem):String = if((elem prefix) eq null) elem label else s"${elem prefix}:${elem label}"

  @tailrec private def instructionsForExprInText(exprs:Seq[TemplateExpr], soFar:String=""):String = exprs match {
    case Seq() => soFar
    case Seq(TemplateExpr.Const(text), rest@_*) =>
      instructionsForExprInText(rest, soFar + s"""("${escapeScalaString(text)}")""")
    case Seq(TemplateExpr.Variable(code), rest@_*) =>
      instructionsForExprInText(rest, soFar + s"""($code)""")
  }

  private def instructionsForExprInAttributeValue(exprs:Seq[TemplateExpr]):String = exprs size match {
    case 0 | 1 => instructionsForExprInAttributeValue0(exprs)
    case _ => s"(${instructionsForExprInAttributeValue0(exprs)})"
  }

  @tailrec private def instructionsForExprInAttributeValue0(exprs:Seq[TemplateExpr], soFar:String=""):String = exprs match {
    case Seq() => if(soFar isEmpty) "\"\"" else soFar
    case Seq(TemplateExpr.Const(text), rest@_*) =>
      val expr = s""""${escapeScalaString(text)}""""
      val newSoFar = if(soFar isEmpty) expr else soFar + " + " + expr
      instructionsForExprInAttributeValue0(rest, newSoFar)
    case Seq(TemplateExpr.Variable(code), rest@_*) =>
      val expr = s"""HTMLAppendable.asString($code, a)"""
      val newSoFar = if(soFar isEmpty) expr else soFar + " + " + expr
      instructionsForExprInAttributeValue0(rest, newSoFar)
  }


  private def isAutoCloseTagElem(elem:Elem):Boolean = ((elem child) isEmpty) && (elem minimizeEmpty)
}
