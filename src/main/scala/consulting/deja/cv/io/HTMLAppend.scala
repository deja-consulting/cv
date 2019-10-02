package consulting.deja.cv.io

import java.nio.charset.Charset

import consulting.deja.cv.language.Language

import scala.annotation.tailrec
import scala.language.postfixOps

/** Allows appending HTML. Some meta properties, such as language and charset, are also available.
 *
 * Out of all of the methods that return `Self`, only one may be called on any given instance. Afterwards, the instance
 * counts as used. Further appending methods need to be called on its return value. */
trait HTMLAppend[+Self<:HTMLAppend[Self]] {
  this:Self=>
  def apply(appendable:HTMLAppendable):Self = appendable(this)
  def apply(str:String):Self

  /** Output charset. */
  def charset:Charset

  /** The language for appending HTML. */
  def language:Language

  /** Appends a newline. */
  def nl:Self = apply("\n")

  /** Language-specific closing quote. */
  def quoteClose:String = (language grammar) quoteClose

  /** Language-specific opening quote. */
  def quoteOpen:String = (language grammar) quoteOpen

  /** Writes the given string to the output document without escaping it. */
  def raw(str:String):Self

  def tagAutoClose(label:String, attributes:(String,String)*):Self
  def tagClose(label:String):Self
  def tagOpen(label:String, attributes:(String,String)*):Self
}
object HTMLAppend {
  def apply[A<:CharAppend[A]](chars:A, language:Language):BasedOnCharAppend[A]= BasedOnCharAppend(chars, language)

  final case class BasedOnCharAppend[A<:CharAppend[A]](chars:A, language:Language) extends HTMLAppend[BasedOnCharAppend[A]] {
    def apply(str:String):BasedOnCharAppend[A] = update(escapeHTML(str, chars))
    def charset:Charset = chars charset
    def raw(str:String):BasedOnCharAppend[A] = update(chars append str)
    def tagAutoClose(label:String, attributes:(String, String)*):BasedOnCharAppend[A] =
      update(tagHead(label, attributes, chars) append "/>")
    def tagClose(label:String):BasedOnCharAppend[A] =
      update(escapeHTMLLabel(label, chars append "</") append '>')
    def tagOpen(label:String, attributes:(String, String)*):BasedOnCharAppend[A] =
      update(tagHead(label, attributes, chars) append ">")
    private def update(chars2:A):BasedOnCharAppend[A] = if(chars eq chars2) this else copy(chars=chars2)
  }

  private val htmlLabelEscape:PartialFunction[Char,String] = {
    case ' ' => ""
    case disallowed if !isAllowedLabelCharacter(disallowed) => "-"
  }
  private val htmlTextEscape:Map[Char,String] = Map('&' -> "&amp;", '<' -> "lt;", '>' -> "&gt;")

  private val htmlAttributeValueEscape:Map[Char,String] = htmlTextEscape ++
    Map('"' -> "&quot;", '\n' -> "&#10;", '\r' -> "&#13;")

  private def escapeHTML[A<:CharAppend[A]](raw:String, out:A):A = escape(raw, 0, htmlTextEscape, out)
  private def escapeHTMLAttributeName[A<:CharAppend[A]](raw:String, out:A) = escapeHTMLLabel(raw, out)
  private def escapeHTMLAttributeValue[A<:CharAppend[A]](raw:String, out:A) = escape(raw, 0, htmlAttributeValueEscape, out)
  private def escapeHTMLLabel[A<:CharAppend[A]](raw:String, out:A) = escape(raw, 0, htmlLabelEscape, out)

  @tailrec private def escape[A<:CharAppend[A]](raw:String, startIndex:Int, fEscape:PartialFunction[Char,String], out:A):A = {
    if(startIndex == (raw length)) out
    else raw indexWhere (fEscape isDefinedAt, startIndex) match {
      case -1 => out append (raw, startIndex, raw length)
      case escapeIndex =>
        val out2 = out append (raw, startIndex, escapeIndex) append fEscape(raw(escapeIndex))
        escape(raw, escapeIndex+1, fEscape, out2)
    }
  }

  private def isAllowedLabelCharacter(c:Char):Boolean =
    (c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='-' || c=='-' || c=='_'

  private def tagHead[A<:CharAppend[A]](label:String, attributes:Iterable[(String,String)], out:A):A = {
    attributes.iterator.foldLeft(escapeHTMLLabel(label, out append '<')) {case (out2, (k,v)) =>
      escapeHTMLAttributeValue(v, escapeHTMLAttributeName(k, out2 append ' ') append "=\"") append '"'
    }
  }
}
