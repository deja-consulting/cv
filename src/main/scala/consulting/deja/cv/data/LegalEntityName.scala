package consulting.deja.cv.data

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}

/** Name of a legal entity, i.e. usually a corporation. */
final case class LegalEntityName(baseName:HTMLAppendable, form:LegalEntityForm) extends HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A = append(form addCommonAbbreviation baseName)
}
