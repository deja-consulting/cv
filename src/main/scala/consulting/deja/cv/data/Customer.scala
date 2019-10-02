package consulting.deja.cv.data

import consulting.deja.cv.data.Country.{Germany, Netherlands}
import consulting.deja.cv.data.LegalEntityForm.{AG, DutchBV, GmbH, Group}
import consulting.deja.cv.template.Phrase._

/** A customer, as client. */
sealed trait Customer extends Client
object Customer {
  case object ConVISUAL extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(ConVISUALName, AG)
    override def nameToday:Option[LegalEntityName] = Some(LegalEntityName(MViseName, AG))
    override def mainCountry:Country = Germany
  }

  case object Debitel extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(DebitelName, AG)
    override def nameToday:Option[LegalEntityName] = Some(LegalEntityName(MobilcomDebitelName, GmbH))
    override def mainCountry:Country = Germany
  }

  case object DeliXL extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(DeliXLName, DutchBV)
    override def nameToday:Option[LegalEntityName] = Some(LegalEntityName(BidfoodName, DutchBV))
    override def mainCountry:Country = Netherlands
  }

  case object GaleriaKaufhof extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(GaleriaKaufhofName, GmbH)
    override def nameToday:Option[LegalEntityName] = None
    override def mainCountry:Country = Germany
  }

  case object ParfuemerieDouglas extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(ParfuemerieDouglasName, GmbH)
    override def nameToday:Option[LegalEntityName] = None
    override def mainCountry:Country = Germany
  }

  case object VorwerkGroup extends Customer {
    override def nameThen:LegalEntityName = LegalEntityName(VorwerkName, Group)
    override def nameToday:Option[LegalEntityName] = None
    override def mainCountry:Country = Germany
  }
}
