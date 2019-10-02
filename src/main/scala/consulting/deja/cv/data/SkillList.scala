package consulting.deja.cv.data

import consulting.deja.cv.data.Skill.OtherSkills
import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}

import scala.language.postfixOps

/** A list of skills, rendered as simple comma-separated list. */
final case class SkillList(skills:Skill*) extends HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A = skills lastOption match {
    case Some(OtherSkills) => ((append language) grammar) enumeration (skills, append)
    case _ =>
      if(skills isEmpty) append
      else (skills tail).foldLeft(append(skills head)) {_(", ")(_)}
  }
}
