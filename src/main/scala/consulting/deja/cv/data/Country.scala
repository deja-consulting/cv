package consulting.deja.cv.data

import consulting.deja.cv.template.Phrase
import consulting.deja.cv.template.Phrase.{GermanyCountryName, NetherlandsCountryName}

sealed trait Country {
  def name:Phrase
}
object Country {
  case object Germany extends Country {def name = GermanyCountryName}
  case object Netherlands extends Country {def name = NetherlandsCountryName}
}
