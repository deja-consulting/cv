package consulting.deja.cv.data

import java.time.LocalDate

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

  /** When the station ended. */
  def end:LocalDate

  /** For the station's title line. */
  def heading:HTMLAppendable

  /** Overview description. */
  def overview:HTMLAppendable

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
    coreSkills:SkillList
  ) extends Station {
    def country:Country = client mainCountry
  }
}
