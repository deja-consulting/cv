package consulting.deja.cv.data

import java.time.LocalDate

import consulting.deja.cv.data.Station.StationCriterion
import consulting.deja.cv.data.Station.StationCriterion.{MajorHighlight, MinorHighlight}
import consulting.deja.cv.io.HTMLAppendable
import consulting.deja.cv.template.Phrase

import scala.language.postfixOps

/** A single CV entry, usually a customer project. */
sealed trait Station {
  /** The customer for whom effort was spent. */
  def client:Client

  /** Core skills used or learned at the station. */
  def coreSkills:SkillList

  /** Where the station was situated. */
  def country:Country

  /** Arbitrary, optional properties of a station. */
  def criteria:Set[StationCriterion]

  /** When the station ended. */
  def end:LocalDate

  /** For the station's title line. */
  def heading:HTMLAppendable

  /** Overview description. */
  def overview:HTMLAppendable

  /** Either the same has `heading`, or optionally a shorter version. */
  def shortHeading:HTMLAppendable

  /** When the station began. */
  def start:LocalDate
}
object Station {
  /** A project for a client. */
  final case class ProjectStation(
    start:LocalDate,
    end:LocalDate,
    client:Client,
    heading:Phrase,
    overview:Phrase,
    coreSkills:SkillList,
    shorterHeading:Option[HTMLAppendable] = None,
    criteria:Set[StationCriterion] = Set.empty
  ) extends Station {
    override def country:Country = client.mainCountry
    def majorHighlight:ProjectStation = copy(criteria = criteria + MajorHighlight)
    def minorHighlight:ProjectStation = copy(criteria = criteria + MinorHighlight)
    override def shortHeading:HTMLAppendable = shorterHeading.getOrElse(heading)
  }

  /** Property that might or might not be present at a certain station. Can be used to filter stations. */
  sealed trait StationCriterion extends (Station=>Boolean)
    {def apply(station:Station):Boolean = station.criteria(this)}
  object StationCriterion {
    case object MajorHighlight extends StationCriterion
    case object MinorHighlight extends StationCriterion
  }
}
