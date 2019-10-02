package consulting.deja.cv.io

import java.nio.charset.Charset

import consulting.deja.cv.language.Language

import scala.language.postfixOps

/** Something that can be appended to a [[CharAppend]]. */
trait CharAppendable {
  def apply[A<:CharAppend[A]](append:A):A
}
object CharAppendable {
  def apply(html:HTMLAppendable, language:Language):CharAppendable = BasedOnHTMLAppendable(html, language)

  def asString(appendable:CharAppendable, charset:Charset):String =
    appendable(new CharAppend.ToString(charset)) getString

  final case class BasedOnHTMLAppendable(html:HTMLAppendable, language:Language) extends CharAppendable {
    def apply[A<:CharAppend[A]](append:A):A = html(HTMLAppend(append, language)) chars
  }
}
