package consulting.deja.cv.data

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}

import scala.language.postfixOps

/** The client of a [[Station]], usually a customer company. */
trait Client {
  /** Including any additions for corporate form or today's name (if changed). */
  def completeName:HTMLAppendable = nameToday match {
    case None => nameThen
    case Some(today) => new HTMLAppendable {
      def apply[A<:HTMLAppend[A]](append:A):A =
        ((append language) grammar) formerNameWithTodaysName (nameThen, today, append)
    }
  }
  /** The name at the time of relevance. Might have changed in the mean time. */
  def nameThen:LegalEntityName

  /** Today's name, in case it changed since [[nameThen]]. */
  def nameToday:Option[LegalEntityName]

  /** Country in which the client is considered to be situated, unless another country is given for a project. */
  def mainCountry:Country
}
