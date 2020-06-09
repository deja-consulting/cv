package consulting.deja.cv.data

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase.TodaysNameShortPrefix

import scala.language.postfixOps

/** The client of a [[Station]], usually a customer company. */
trait Client {
  /** Including any additions for corporate form or today's name (if changed). */
  def completeName:HTMLAppendable = nameToday match {
    case None => nameThen
    case Some(today) => new HTMLAppendable {
      def apply[A<:HTMLAppend[A]](append:A):A = {
        val nameToday = if(today.form == nameThen.form) today.baseName else today
        append.language.grammar.formerNameWithTodaysName(nameThen, nameToday, append)
      }
    }
  }

  /** The name at the time of relevance. Might have changed in the mean time. */
  def nameThen:LegalEntityName

  /** Today's name, in case it changed since [[nameThen]]. */
  def nameToday:Option[LegalEntityName]

  /** Country in which the client is considered to be situated, unless another country is given for a project. */
  def mainCountry:Country

  /** A version of the name that consumes less space than `completeName`. */
  def shortName:HTMLAppendable = nameToday match {
    case None => nameThen
    case Some(today) =>
      nameThen.baseName ++ HTMLAppendable(", ") ++ TodaysNameShortPrefix ++ HTMLAppendable(" ") ++ today.baseName
  }
}
