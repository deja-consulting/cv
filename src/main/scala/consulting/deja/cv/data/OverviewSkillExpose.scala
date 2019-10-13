package consulting.deja.cv.data

import consulting.deja.cv.data.Skill._
import consulting.deja.cv.template.Phrase
import consulting.deja.cv.template.Phrase._

/** All categorized skills, as shown on the standard CV. */
object OverviewSkillExpose {
  val categories:Seq[(Phrase,Either[SkillList,Seq[(Phrase,SkillList)]])] = Seq(
    Skills -> Right(Seq(
      ProgrammingLanguages -> SkillList(ScalaSkill, JavaSkill, JavaScriptSkill, OtherSkills),
      Technologies -> SkillList(AWSSkill, KubernetesSkill, SpringSkill, KafkaSkill, AkkaSkill, DockerSkill),
      Concepts -> SkillList(AgileSkill, MicroservicesSkill, FunctionalProgrammingSkill, TDD_BDDSkill)
    )),
    Roles -> Left(SkillList(SoftwareDeveloperRole, SystemArchitectRole, BusinessAnalystRole, ScrumMasterRole))
  )
}
