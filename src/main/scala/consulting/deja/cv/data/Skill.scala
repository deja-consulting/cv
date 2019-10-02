package consulting.deja.cv.data

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase
import consulting.deja.cv.template.Phrase._

/** Model for job skills. */
final case class Skill(phrase:Phrase) extends HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A = append(phrase)
}
object Skill {
  val AgileSkill = Skill(Agile)
  val AgileWithScrumAndKanbanSkill = Skill(AgileWithScrumAndKanban)
  val AkkaClusterSkill = Skill(AkkaCluster)
  val AkkaSkill = Skill(Akka)
  val ApacheSparkSkill = Skill(ApacheSpark)
  val ATGSkill = Skill(ATGSkillDescription)
  val B2BApplicationDevelopmentSkill = Skill(B2BApplicationDevelopment)
  val BDDSkill = Skill(BDDAcronym)
  val BusinessAnalystRole = Skill(BusinessAnalyst)
  val CassandraSkill = Skill(Cassandra)
  val DockerSkill = Skill(Docker)
  val GigaSpacesSkill = Skill(GigaSpaces)
  val HazelcastSkill = Skill(Hazelcast)
  val HibernateSkill = Skill(Hibernate)
  val HighAvailabilitySkill = Skill(HighAvailability)
  val HighPerformanceSkill = Skill(HighPerformance)
  val HornetQSkill = Skill(HornetQ)
  val HybrisIntegrationSkill = Skill(HybrisIntegration)
  val JBehaveSkill = Skill(JBehave)
  val KeycloakSkill = Skill(Keycloak)
  val JavaEESkill = Skill(JavaEE)
  val JavaScriptSkill = Skill(JavaScript)
  val JavaSkill = Skill(Java)
  val KafkaSkill = Skill(Kafka)
  val MicroservicesSkill = Skill(Microservices)
  val MobileSkill = Skill(MobileSkillDescription)
  val MySQLSkill = Skill(MySQL)
  val NetflixStackSkill = Skill(NetflixStack)
  val OmnichannelSkill = Skill(Omnichannel)
  val OracleSkill = Skill(Oracle)
  val OtherSkills = Skill(Others)
  val PlayFrameworkSkill = Skill(PlayFramework)
  val ResponsiveSkill = Skill(Responsive)
  val ScalaSkill = Skill(Scala)
  val ScrumMasterRole = Skill(ScrumMaster)
  val SoftwareDeveloperRole = Skill(SoftwareDeveloper)
  val SpringSkill = Skill(Spring)
  val SystemArchitectRole = Skill(SystemArchitect)
  val TDD_BDDSkill = Skill(TDD_BDD)
  val TDDSkill = Skill(TDDAcronym)
  val WaterfallSkill = Skill(WaterfallSkillDescription)
}
