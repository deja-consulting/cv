package consulting.deja.cv.language

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}

/** Natural-language enumeration, just like in [[Grammar.enumeration]]. */
final case class Enumeration(elements:Seq[HTMLAppendable]) extends HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A = append.language.grammar.enumeration(elements, append)
}
