package consulting.deja.cv.variant.rendering

import java.nio.charset.Charset

import consulting.deja.cv.io.HTMLAppendable
import consulting.deja.cv.language.Language
import consulting.deja.cv.template.Phrase.{CurriculumVitae, SubjectFirstName, SubjectLastName}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

case class StandardOverviewPageCSS(mainCSS:StandardOverviewCSS, language:Language, charset:Charset) extends StyleSheet.Inline {
  import dsl._
  import mainCSS._

  lazy val renderedAsString:String =
    replacePlaceholder(mainPageStyle.inspect.replace(""".???""", "@page")).replace("}\n", "\n  " + replacePlaceholder(render).replaceAll("\n", "\n  ").trim) + "}\n\n"
  private val placeholderForAt:String = "PlaceholderForAt"

  private def replacePlaceholder(str:String):String = str.replaceAll(s"""\\.$placeholderForAt-""", "@")

  private def asString(appendable:HTMLAppendable):String = HTMLAppendable.asString(appendable, language, charset)

  private val mainPageStyle:StyleS = measures.pageMargins

  val bottomCenter:StyleA = style(s"$placeholderForAt-bottom-center")(
    content := "counter(page)",
    footerPart,
    width(100.%%)
  )

  val bottomLeft:StyleA = style(s"$placeholderForAt-bottom-left")(
    content := s"'${asString(SubjectFirstName)} ${asString(SubjectLastName)}'",
    footerPart,
    width(measures.pageFooterSideWidth)
  )

  val bottomRight:StyleA = style(s"$placeholderForAt-bottom-right")(
    content := s"'${asString(CurriculumVitae)}'",
    footerPart,
    width(measures.pageFooterSideWidth)
  )

  val topCenter:StyleA = style(s"$placeholderForAt-top-center")(
    content := "''",
    borderBottom(colors.mainContrast, solid, measures.pageBorderThickness),
    width(100.%%),
    marginBottom(measures.pageHeaderMargin)
  )

  private lazy val footerPart:StyleS = mixin(
    fonts.main,
    measures.mainFontSize,
    borderBottom(colors.mainContrast, solid, measures.pageBorderThickness),
    marginBottom(measures.pageFooterMargin),
    paddingBottom(measures.pageFooterPadding)
  )
}
