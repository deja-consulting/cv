package consulting.deja.cv.language

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase.{And, TodaysNameShortPrefix}

/** Linguistic rules of a certain language. */
sealed trait Grammar {
  /** Natural-language enumeration. In English, this would separate all element with a comma, except the last two with
   * the word "and". This is also the default implementation. */
  def enumeration[A<:HTMLAppend[A]](elements:Seq[HTMLAppendable], append:A):A = elements match {
    case Seq() => append
    case Seq(onlyOne) => append(onlyOne)
    case Seq(first, second) => append(first)(" ")(And)(" ")(second)
    case Seq(first, rest@_*) => enumeration(rest, append(first)(", "))
  }

  /** Combines a former name with its name today, after a name change. */
  def formerNameWithTodaysName[A<:HTMLAppend[A]](formerName:HTMLAppendable, todaysName:HTMLAppendable, append:A):A =
    append(formerName)(" (")(TodaysNameShortPrefix)(" ")(todaysName)(")")

  /** Language-specific closing quote. */
  def quoteClose:String

  /** Language-specific opening quote. */
  def quoteOpen:String
}
object Grammar {
  /** Grammar of the English language. */
  object English extends Grammar {
    def quoteClose:String = "”"
    def quoteOpen:String = "“"
  }

  /** Grammar of the German language. */
  object German extends Grammar {
    def quoteClose:String = "“"
    def quoteOpen:String = "„"
  }
}
