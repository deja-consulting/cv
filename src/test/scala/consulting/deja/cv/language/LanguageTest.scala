package consulting.deja.cv.language

import org.scalatest.{FreeSpec, Matchers}

import scala.language.postfixOps

class LanguageTest extends FreeSpec with Matchers {
  "all languages have" - {
    "different" - {
      "english names" in {Language.all map (_ englishName) should have size (Language.all size)}
      "toString representations" in {Language.all map (_ toString) should have size (Language.all size)}
    }
    "non-empty" - {
      "english names" in {Language.all map (_ englishName) should not contain ""}
      "toString representations" in {Language.all map (_ toString) should not contain ""}
    }
  }
}
