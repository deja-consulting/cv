package consulting.deja.cv.data

import java.time.LocalDate
import java.time.Month._

import consulting.deja.cv.data.Customer._
import consulting.deja.cv.data.Station.ProjectStation
import consulting.deja.cv.template.Phrase._

import scala.collection.SortedMap

/** Lists all CV stations. */
object Stations {
  /** From latest to oldest. */
  val all:Seq[Station] =
    y2018_all ++ y2017_all ++ y2016_all ++ y2015_all ++ y2014_all ++ y2011_all ++ y2002_all ++ y2001_all

  /** Categorized by client, in no particular order of client. */
  val byClient:Map[Client,Seq[Station]] = all.groupBy(_.client)

  private implicit val localDateOrdering:Ordering[LocalDate] = Ordering.fromLessThan {_.isBefore(_)}

  /** Categorized by client, in proper order of clients for the overview. */
  val forOverview:SortedMap[Client,Seq[Station]] = SortedMap(byClient.toSeq:_*)(Ordering.fromLessThan {(a, b) =>
    byClient(a).map(_.end).max.isAfter(byClient(b).map(_.end).max)
  })

  import Skill._

  private lazy val y2018_all:Seq[Station] = Vector(y2018_moi_pay)
  private lazy val y2018_moi_pay = ProjectStation(
    client = MOIA,
    start = LocalDate.of(2018, SEPTEMBER, 17), end = LocalDate.of(2019, SEPTEMBER, 13),
    heading = StationMOIPAYHeading, overview = StationMOIPAYOverview,
    coreSkills = SkillList(ScalaSkill, AWSSkill, KubernetesSkill, AkkaSkill, FunctionalProgrammingSkill, AccountingSkill)
  ).majorHighlight

  private lazy val y2017_all:Seq[Station] = Vector(y2017_gkh_idp)
  private lazy val y2017_gkh_idp = ProjectStation(
    client = GaleriaKaufhof,
    start = LocalDate.of(2017, MAY, 10), end = LocalDate.of(2018, APRIL, 6),
    heading = StationGKHIDPHeading, shorterHeading = Some(StationGKHIDPHeadingShort), overview = StationGKHIDPOverview,
    coreSkills = SkillList(ScalaSkill, AkkaClusterSkill, CassandraSkill, PlayFrameworkSkill, KeycloakSkill, JavaSkill)
  ).majorHighlight

  private lazy val y2016_all:Seq[Station] = Vector(y2016_dou_mig)
  private lazy val y2016_dou_mig = ProjectStation(
    client = ParfuemerieDouglas,
    start = LocalDate.of(2016, MAY, 10), end = LocalDate.of(2017, MARCH, 3),
    heading = StationDOUMIGHeading, shorterHeading = Some(StationDOUMIGHeadingShort), overview = StationDOUMIGOverview,
    coreSkills = SkillList(ScalaSkill, KafkaSkill, SpringSkill, HornetQSkill)
  ).majorHighlight

  private lazy val y2015_all:Seq[Station] = Vector(y2015_dou_kpi, y2015_dou_sso, y2015_vor_ga)
  private lazy val y2015_dou_kpi = ProjectStation(
    client = ParfuemerieDouglas,
    start = LocalDate.of(2015, JUNE, 1), end = LocalDate.of(2015, SEPTEMBER, 15),
    heading = StationDOUKPIHeading, overview = StationDOUKPIOverview,
    coreSkills = SkillList(ScalaSkill, KafkaSkill, AkkaSkill, ApacheSparkSkill, HybrisIntegrationSkill)
  ).minorHighlight
  private lazy val y2015_dou_sso = ProjectStation(
    client = ParfuemerieDouglas,
    start = LocalDate.of(2015, APRIL, 15), end = LocalDate.of(2015, SEPTEMBER, 14),
    heading = StationDOUSSOHeading, shorterHeading = Some(StationDOUSSOHeadingShort), overview = StationDOUSSOOverview,
    coreSkills = SkillList(ScalaSkill, AkkaSkill, MicroservicesSkill, SpringSkill, HornetQSkill)
  ).minorHighlight
  private lazy val y2015_vor_ga = ProjectStation(
    client = VorwerkGroup,
    start = LocalDate.of(2015, JANUARY, 5), end = LocalDate.of(2015, APRIL, 5),
    heading = StationVORGAHeading, overview = StationVORGAOverview,
    coreSkills = SkillList(HighAvailabilitySkill, MicroservicesSkill, HazelcastSkill, SpringSkill, DockerSkill, NetflixStackSkill)
  ).majorHighlight

  private lazy val y2014_all:Seq[Station] = Vector(y2014_vor_erp)
  private lazy val y2014_vor_erp = ProjectStation(
    client = VorwerkGroup,
    start = LocalDate.of(2014, MARCH, 15), end = LocalDate.of(2015, JANUARY, 4),
    heading = StationVORERPHeading, overview = StationVORERPOverview,
    coreSkills = SkillList(HighAvailabilitySkill, HighPerformanceSkill, SpringSkill, TDD_BDDSkill, HibernateSkill, JBehaveSkill)
  ).minorHighlight

  private lazy val y2011_all:Seq[Station] = Vector(y2011_dxl)
  private lazy val y2011_dxl = ProjectStation(
    client = DeliXL,
    start = LocalDate.of(2011, APRIL, 4), end = LocalDate.of(2014, FEBRUARY, 5),
    heading = StationDXLHeading, shorterHeading = Some(StationDXLHeadingShort), overview = StationDXLOverview,
    coreSkills = SkillList(AgileSkill, TDDSkill, ATGSkill, JavaSkill, OracleSkill, GigaSpacesSkill, OmnichannelSkill, ResponsiveSkill)
  ).majorHighlight

  private lazy val y2002_all:Seq[Station] = Vector(y2002_cv)
  private lazy val y2002_cv = ProjectStation(
    client = ConVISUAL,
    start = LocalDate.of(2002, APRIL, 1), end = LocalDate.of(2010, JUNE, 30),
    heading = StationCVHeading, overview = StationCVOverview,
    coreSkills = SkillList(JavaSkill, MobileSkill, B2BApplicationDevelopmentSkill, WaterfallSkill)
  ).minorHighlight

  private lazy val y2001_all:Seq[Station] = Vector(y2001_deb)
  private lazy val y2001_deb = ProjectStation(
    client = Debitel,
    start = LocalDate.of(2001, JULY, 1), end = LocalDate.of(2002, JANUARY, 22),
    heading = StationDEBHeading, overview = StationDEBOverview,
    coreSkills = SkillList()
  )
}
