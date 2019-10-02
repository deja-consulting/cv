package consulting.deja.cv.io

import java.nio.charset.Charset

import consulting.deja.cv.language.Language

import scala.language.postfixOps

/** Something that can be appended to an [[HTMLAppend]]. */
trait HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A
  def toCharAppendableFor(language:Language):CharAppendable = CharAppendable(this, language)

  def ++(that:HTMLAppendable):HTMLAppendable = new HTMLAppendable {
    override def apply[A<:HTMLAppend[A]](append:A):A = that(HTMLAppendable.this(append))
  }
}
object HTMLAppendable {
  def apply(i:Int):HTMLAppendable = new HTMLAppendable {
    override def apply[A<:HTMLAppend[A]](append:A):A = append(i.toString)
  }
  def apply(s:String):HTMLAppendable = new HTMLAppendable {
    override def apply[A<:HTMLAppend[A]](append:A):A = append(s)
  }

  def asString(appendable:HTMLAppendable, language:Language, charset:Charset):String =
    CharAppendable asString (appendable toCharAppendableFor language, charset)
  def asString(appendable:HTMLAppendable, model:HTMLAppend[_]):String =
    asString(appendable, model language, model charset)

  val empty:HTMLAppendable = new HTMLAppendable {
    override def apply[A<:HTMLAppend[A]](append:A):A = append
  }
}
