package consulting.deja.cv.language

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class LanguageTest extends AnyFreeSpec with Matchers:
  "all languages have" - {
    "different" - {
      "english names" in { Language.all map (_ englishName) should have size (Language.all size) }
      "toString representations" in { Language.all map (_ toString) should have size (Language.all size) }
    }
    "non-empty" - {
      "english names" in { Language.all map (_ englishName) should not contain "" }
      "toString representations" in { Language.all map (_ toString) should not contain "" }
    }
  }
