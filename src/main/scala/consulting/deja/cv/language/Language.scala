package consulting.deja.cv.language

import java.util.Locale

import scala.collection.immutable.ListSet

/** Refers to a certain language. */
sealed trait Language {
  def englishName:String
  def grammar:Grammar
  /** The ISO 639 language code. */
  def isoCode:String
}
object Language {
  val all:Set[Language] = ListSet(English, German)

  object English extends Language {
    def englishName = "english"
    def grammar:Grammar = Grammar.English
    def isoCode:String = Locale.ENGLISH.getLanguage
  }

  object German extends Language {
    def englishName = "german"
    def grammar:Grammar = Grammar.German
    def isoCode:String = Locale.GERMAN.getLanguage
  }
}
