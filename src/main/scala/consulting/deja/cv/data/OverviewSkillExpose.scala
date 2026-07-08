package consulting.deja.cv.data

import consulting.deja.cv.data.Skill.*
import consulting.deja.cv.template.Phrase
import consulting.deja.cv.template.Phrase.*

/** All categorized skills, as shown on the standard CV. */
object OverviewSkillExpose:
  val categories: Seq[(Phrase, Either[SkillList, Seq[(Phrase, SkillList)]])] = Seq(
    Skills -> Right(
      Seq(
        ProgrammingLanguages -> SkillList(
          ScalaSkill,
          JavaSkill,
          KotlinSkill,
          JavaScriptSkill,
          TypeScriptSkill,
          OtherSkills,
        ),
        Technologies -> SkillList(AWSSkill, ACSSkill, KubernetesSkill, KafkaSkill, DockerSkill),
        WorkMethods -> SkillList(AgileSkill, DDDSkill, TDD_BDDSkill),
        Concepts -> SkillList(MicroservicesSkill, AsynchronousProgrammingSkill, CICDSkill)
      )),
    Roles -> Left(SkillList(SoftwareDeveloperRole, SystemArchitectRole, BusinessAnalystRole, ScrumMasterRole))
  )
