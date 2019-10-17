package consulting.deja.cv.variant.rendering

import java.time.Duration

import consulting.deja.cv.data
import consulting.deja.cv.data.{Client, OverviewSkillExpose, Subject}
import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.language.ApproximateDuration
import consulting.deja.cv.template.HTMLDocument
import consulting.deja.cv.template.standardoverview.StandardOverviewTemplates._

import scala.language.postfixOps

/** Renders the document in the [[consulting.deja.cv.variant.Variant.StandardOverview]] variant. */
object StandardOverviewHTML {
  val root = HTMLDocument.Root(Title, HeaderAdditions, Body(SkillsExposeTable(SkillsExposeRows), socialLinks, stations))

  private def clientHeadingAppendable(client:Client):HTMLAppendable = client.mainCountry match {
    case notToMention if notToMention == Subject.baseCountry => client.completeName
    case _ => client.completeName ++ HTMLAppendable(", ") ++ client.mainCountry.name
  }

  private object SkillsExposeRows extends HTMLAppendable {
    def apply[A<:HTMLAppend[A]](append:A):A =
      (OverviewSkillExpose categories).foldLeft(append nl) {case (a1, (catName, contents)) => contents match {
        case Left(skills) => skillsExposeRowWithoutSubcat(catName, skills, a1)
        case Right(subcat) => (subcat zipWithIndex).foldLeft(a1) {case (a2, ((subcatName, skills), index)) =>
          if(index==0) skillsExposeFirstRowWithSubcat(catName, subcatName, skills, a2)
          else skillsExposeFollowingRowWithSubcat(subcatName, skills, a2)
        }
      }}
  }

  private def socialLinks:HTMLAppendable = SocialLinkList(
    Subject.socialLinks.map(socialLink => SocialLink(socialLink.icon, HTMLAppendable(socialLink.url)))
      .foldLeft(HTMLAppendable.empty)(_ ++ _)
  )

  private def stationAppendable(station:data.Station):HTMLAppendable = Station(
    heading = station.heading,
    duration = ApproximateDuration(Duration.between(station.start.atStartOfDay, station.end.atStartOfDay)),
    year = HTMLAppendable(station.start.getYear),
    overview = station.overview,
    coreSkills = if(station.coreSkills.skills.nonEmpty) StationCoreSkills(station.coreSkills) else HTMLAppendable.empty
  )

  private def stationListAppendable(client:Client, stations:Seq[data.Station]):HTMLAppendable = StationList(
    clientHeadingAppendable(client),
    stations.map(stationAppendable).foldLeft(HTMLAppendable.empty)(_ ++ _)
  )

  private def stations:HTMLAppendable = data.Stations.forOverview.foldLeft(HTMLAppendable.empty) {
    case (acc, (client, stationList)) => acc ++ stationListAppendable(client, stationList)
  }
}
