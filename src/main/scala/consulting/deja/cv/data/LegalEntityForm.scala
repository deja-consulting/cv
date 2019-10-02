package consulting.deja.cv.data

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase.{AGAcronym, DutchBVAbbreviation, GmbHAbbreviation, GroupEntityType}

/** The form of a legal entity, for example a company. */
sealed trait LegalEntityForm {
  /** Modifies the given base name with the common abbreviated phrase for the legal entity form. Some entity forms,
   * like people, may return the base name unmodified. */
  def addCommonAbbreviation(baseName:HTMLAppendable):HTMLAppendable
}
object LegalEntityForm {
  /** German "Aktiengesellschaft". */
  case object AG extends LegalEntityForm {
    override def addCommonAbbreviation(baseName:HTMLAppendable):HTMLAppendable =
      new HTMLAppendable {override def apply[A<:HTMLAppend[A]](append:A):A = append(baseName)(" ")(AGAcronym)}
  }

  /** Dutch "B.V." */
  case object DutchBV extends LegalEntityForm {
    override def addCommonAbbreviation(baseName:HTMLAppendable):HTMLAppendable =
      new HTMLAppendable {override def apply[A<:HTMLAppend[A]](append:A):A = append(baseName)(" ")(DutchBVAbbreviation)}
  }

  /** German GmbH */
  case object GmbH extends LegalEntityForm {
    def addCommonAbbreviation(baseName:HTMLAppendable):HTMLAppendable =
      new HTMLAppendable {def apply[A<:HTMLAppend[A]](append:A):A = append(baseName)(" ")(GmbHAbbreviation)}
  }

  /** Company group, might consist out of several legal entities. */
  case object Group extends LegalEntityForm {
    override def addCommonAbbreviation(baseName:HTMLAppendable):HTMLAppendable =
      new HTMLAppendable {override def apply[A<:HTMLAppend[A]](append:A):A = append(baseName)(" ")(GroupEntityType)}
  }
}
