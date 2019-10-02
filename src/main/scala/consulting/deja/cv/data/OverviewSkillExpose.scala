package consulting.deja.cv.data

import consulting.deja.cv.data.Skill._
import consulting.deja.cv.template.Phrase
import consulting.deja.cv.template.Phrase._

/** All categorized skills, as shown on the standard CV. */
object OverviewSkillExpose {
  val categories:Seq[(Phrase,Either[SkillList,Seq[(Phrase,SkillList)]])] = Seq(
    Skills -> Right(Seq(
      ProgrammingLanguages -> SkillList(JavaSkill, ScalaSkill, JavaScriptSkill, OtherSkills),
      Technologies -> SkillList(JavaEESkill, SpringSkill, KafkaSkill, AkkaSkill, ApacheSparkSkill, DockerSkill),
      Databases -> SkillList(OracleSkill, MySQLSkill, CassandraSkill, HazelcastSkill, GigaSpacesSkill),
      Concepts -> SkillList(AgileWithScrumAndKanbanSkill, MicroservicesSkill, TDDSkill, BDDSkill)
    )),
    Roles -> Left(SkillList(SystemArchitectRole, SoftwareDeveloperRole, ScrumMasterRole, BusinessAnalystRole))
  )
}
