package consulting.deja.cv.data

import consulting.deja.cv.data.Country.Germany
import consulting.deja.cv.io.HTMLAppendable
import consulting.deja.cv.template.Phrase.{GermanNationalityMale, SoftwareArchitectAndDeveloper}

/** Data on the subject of the CV, i.e. the original author of this code. */
object Subject {
  def baseCountry:Country = Germany
  def nationality:HTMLAppendable = GermanNationalityMale
  def primaryJobTitle:HTMLAppendable = SoftwareArchitectAndDeveloper
}
