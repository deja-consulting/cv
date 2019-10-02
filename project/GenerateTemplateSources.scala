import java.io.File

import sbt._

import scala.language._

object GenerateTemplateSources {
  def apply(sourceBase:File, targetBase:File):Seq[File] = {
    val templateGroups = TemplateParser(sourceBase / "resources" / "html-templates")
    println(s"Found template groups: ${templateGroups map (_ name) mkString ", "}")
    TemplateRenderer(templateGroups, targetBase) ++ DictionaryRenderer(templateGroups flatMap (_ phrases), targetBase)
  }
}
