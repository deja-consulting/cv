package consulting.deja.cv.language

import consulting.deja.cv.data.Subject
import consulting.deja.cv.io.HTMLAppend
import consulting.deja.cv.template.Dictionary.TechnicalPhrases
import consulting.deja.cv.variant.rendering.StandardOverviewCSS

trait TechnicalPhrasesTranslation extends TechnicalPhrases {
  override def appendCharsetISOCode[A<:HTMLAppend[A]](append:A):A = append(append.charset.name)
  override def appendLanguageISOCode[A<:HTMLAppend[A]](append:A):A = append(append.language.isoCode)
  override def appendStandardOverviewCSS[A<:HTMLAppend[A]](append:A):A =
    append.raw(StandardOverviewCSS(append.language, append.charset).renderedAsString)
  override def appendStandardOverviewExternalStylesheets[A<:HTMLAppend[A]](append:A):A =
    StandardOverviewCSS(append.language, append.charset).fontCSSRefs.foldLeft(append) {(a, ref) =>
      a.tagAutoClose("link", "rel"->"stylesheet", "type"->"text/css", "href"->ref)
    }
  override def appendSubjectNationality[A<:HTMLAppend[A]](append:A):A = Subject.nationality(append)
}
