package consulting.deja.cv.variant.rendering

import java.nio.charset.Charset

import consulting.deja.cv.data.Station.StationCriterion.{MajorHighlight, MinorHighlight}
import consulting.deja.cv.data.{OverviewSkillExpose, Station, Stations}
import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.language.Language
import consulting.deja.cv.template.Phrase.StandardIntroProse
import consulting.deja.cv.template.website.WebsiteTemplates._

/** Renders the fragments used for the website. */
object WebsiteHTML {
  val all:Map[String,HTMLAppendable] = Map(
    "contact/confirmation" -> ContactConfirmation,
    "contact/form" -> ContactForm,
    "cv-download" -> CvDownload,
    "imprint" -> WebsiteImprint,
    "person" -> Person,
    "skills-expose" -> skillsExpose,
    "stations/highlights" -> majorStations,
    "stations/smaller" -> minorStations,
    "work" -> StandardIntroProse
  )

  private lazy val majorStations:HTMLAppendable = stationListAppendable(Stations.all.filter(MajorHighlight))

  private lazy val minorStations:HTMLAppendable = stationListAppendable(Stations.all.filter(MinorHighlight))

  private lazy val skillsExpose:HTMLAppendable = SkillsExposeTable(
    OverviewSkillExpose.categories.map {
      case (_, Left(_)) => Seq.empty
      case (_, Right(skillsByCategory)) => skillsByCategory
    }.flatten.map {
      case (category, skills) => SkillsExposeTableRow(category, skills)
    }.foldLeft(HTMLAppendable.empty)(_ ++ _)
  )

  private def stationListAppendable(stations:Iterable[Station]):HTMLAppendable = {
    def stationAppendable(station:Station):HTMLAppendable = {
      def description:HTMLAppendable = new HTMLAppendable {
        override def apply[A<:HTMLAppend[A]](append:A):A =
          new FlatteningAppendRoot[A](append)(StationDescription(station.overview, station.coreSkills)).base
      }
      def heading:HTMLAppendable = StationHeading(station.shortHeading, station.client.shortName, time)
      def time:HTMLAppendable = yearRange(station.start.getYear, station.end.getYear)
      heading ++ description
    }
    StationList(stations.foldLeft(HTMLAppendable.empty)(_ ++ stationAppendable(_)))
  }

  private def yearRange(fromYear:Int, untilYear:Int):HTMLAppendable = HTMLAppendable {
    lazy val fromString = fromYear.toString
    lazy val untilString = untilYear.toString
    def sameCentury:Boolean = fromYear>0 && fromString.dropRight(2) == untilString.dropRight(2)
    if(fromYear == untilYear) fromString
    else if(fromYear+1==untilYear && sameCentury) s"$fromString/${untilString.takeRight(2)}"
    else if(sameCentury) s"$fromString-'${untilString.takeRight(2)}"
    else s"$fromString-$untilString"
  }

  /** Base type for an appender that wraps another one. */
  private sealed trait WrappedAppend[Base<:HTMLAppend[Base]] extends HTMLAppend[WrappedAppend[Base]] {
    override def apply(str:String):WrappedAppend[Base] = withBase(base.apply(str))
    override def charset:Charset = base.charset
    override def language:Language = base.language
    override def raw(str:String):WrappedAppend[Base] = withBase(base.raw(str))

    def base:Base
    protected[rendering] def withBase(newBase:Base):WrappedAppend[Base]
  }

  /** Filters the appended output to filter out unordered lists, and convert list items an paragraphs into simple text
   * followed by a line break. */
  private class FlatteningAppendRoot[Base<:HTMLAppend[Base]](val base:Base) extends WrappedAppend[Base] {
    override protected[rendering] def withBase(newBase:Base):WrappedAppend[Base] = new FlatteningAppendRoot(newBase)
    override def tagAutoClose(label:String, attributes:(String, String)*):WrappedAppend[Base] =
      withBase(base.tagAutoClose(label, attributes:_*))
    override def tagClose(label:String):WrappedAppend[Base] = sys.error(s"Cannot close tag that was not opened: $label")
    override def tagOpen(label:String, attributes:(String, String)*):WrappedAppend[Base] = label match {
      case "p" | "li" => new FlatteningAppendFlattened[Base](base, this)
      case "ul" => new FlatteningAppendIgnored(base, this)
      case _ => new FlatteningAppendChild(base.tagOpen(label, attributes:_*), this)
    }
  }

  /** Like root, except inside an open child tag (that is neither ignored nor itself flattened). */
  private class FlatteningAppendChild[Base<:HTMLAppend[Base]](base:Base, parent:FlatteningAppendRoot[Base])
  extends FlatteningAppendRoot(base) {
    override protected[rendering] def withBase(newBase:Base):WrappedAppend[Base] =
      new FlatteningAppendChild(base, parent)
    override def tagClose(label:String):WrappedAppend[Base] = parent.withBase(base.tagClose(label))
  }

  /** Like root, except inside a tag that is ignored (but the content is not ignored). */
  private class FlatteningAppendIgnored[Base<:HTMLAppend[Base]](base:Base, parent:FlatteningAppendRoot[Base])
  extends FlatteningAppendRoot(base) {
    override protected[rendering] def withBase(newBase:Base):WrappedAppend[Base] =
      new FlatteningAppendIgnored(newBase, parent)
    override def tagClose(label:String):WrappedAppend[Base] = parent.withBase(base)
  }

  /** Like root, except inside a tag that is flattened. That means that the tag gets ignored (but not its contents),
   * and instead of closing the tag, a line break gets added. */
  private class FlatteningAppendFlattened[Base<:HTMLAppend[Base]](base:Base, parent:FlatteningAppendRoot[Base])
  extends FlatteningAppendRoot(base) {
    override protected[rendering] def withBase(newBase:Base):WrappedAppend[Base] =
      new FlatteningAppendFlattened(newBase, parent)
    override def tagClose(label:String):WrappedAppend[Base] = parent.withBase(base.tagAutoClose("br"))
  }
}
