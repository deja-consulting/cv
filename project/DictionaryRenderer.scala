import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets.UTF_8

object DictionaryRenderer {
  val LanguageNameInvariant = "LanguageInvariant"
  val LanguageNameTechnical = "Technical"
  
  def apply(phrases:Traversable[TranslationPhrase], baseDirectory:File):Seq[File] = {
    val targetFolder = new File(baseDirectory, s"${(TemplateRenderer TemplateBasePackage) replaceAllLiterally (".", "/")}")
    targetFolder mkdirs()
    val phrasesTargetFile = new File(targetFolder, "Phrase.scala")
    val dictionaryTargetFile = new File(targetFolder, "Dictionary.scala")
    val phrasesOutputStream = new FileOutputStream(phrasesTargetFile)
    try {
      val dictionaryOutputStream = new FileOutputStream(dictionaryTargetFile)
      try {
        val phrasesWriter = new BufferedWriter(new OutputStreamWriter(phrasesOutputStream, UTF_8))
        val dictionaryWriter = new BufferedWriter(new OutputStreamWriter(dictionaryOutputStream, UTF_8))
        renderPhraseFileContents(phrases, phrasesWriter, dictionaryWriter)
        phrasesWriter flush(); dictionaryWriter flush()
      } finally {dictionaryOutputStream close()}
    } finally {phrasesOutputStream close()}
    Seq(phrasesTargetFile, dictionaryTargetFile)
  }
  
  private def renderPhraseFileContents(phrases:Traversable[TranslationPhrase], phraseOut:Appendable, dictionaryOut:Appendable) {
    val (languageVariantPhrases, specialPhrases) =
      phrases partition (p => p.language != LanguageNameInvariant && p.language != LanguageNameTechnical)
    val (languageInvariantPhrases, technicalPhrases) = specialPhrases partition (_.language == LanguageNameInvariant)

    phraseOut append s"package ${TemplateRenderer TemplateBasePackage}\n\n"
    phraseOut append
      """import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
        |
        |sealed trait Phrase extends HTMLAppendable
        |object Phrase {
        |  sealed trait LanguageInvariant extends Phrase
        |  sealed trait LanguageVariant extends Phrase
        |  sealed trait Technical extends Phrase
        |
        |""".stripMargin
    renderPhraseConstantDeclarations(languageVariantPhrases map (_ key) toSet, languageInvariantPhrases map (_ key), technicalPhrases map (_ key), phraseOut)
    phraseOut append "}\n"
    
    dictionaryOut append s"package ${TemplateRenderer TemplateBasePackage}\n\n"
    dictionaryOut append
      """import consulting.deja.cv.io.HTMLAppend
        |import consulting.deja.cv.language.{Language, TechnicalPhrasesTranslation}
        |
        |object Dictionary {
        |  import Phrase._
        |""".stripMargin
    dictionaryOut append
      """
        |  trait TechnicalPhrases {
        |""".stripMargin
    (technicalPhrases toVector) sortBy (_ key) foreach {phrase =>
      dictionaryOut append s"    def append${phrase key}[A<:HTMLAppend[A]](append:A):A\n"
    }
    dictionaryOut append
      """  }
        |  object TechnicalPhrases extends TechnicalPhrases with TechnicalPhrasesTranslation
        |""".stripMargin
    ((languageVariantPhrases groupBy (_ key)) toVector) sortBy (_ _1) foreach {case (key, phrasesPerLanguage) =>
      dictionaryOut append
        s"""
           |  def append$key[A<:HTMLAppend[A]](append:A):A = {
           |    var a = append
           |    append.language match {
           |""".stripMargin
      (phrasesPerLanguage groupBy (_ language) mapValues (_ head)) foreach {case (language, phrase) =>
        dictionaryOut append s"      case Language.$language =>\n"
        (phrase instructions) foreach {instr => dictionaryOut append s"        $instr\n"}
      }
      dictionaryOut append
        """    }
          |    a
          |  }
          |""".stripMargin
    }
    (languageInvariantPhrases toVector) sortBy (_ key) foreach {phrase =>
      dictionaryOut append
        s"""
           |  def append${phrase key}[A<:HTMLAppend[A]](append:A):A = {
           |    var a = append
           |""".stripMargin
      (phrase instructions) foreach {instr => dictionaryOut append s"    $instr\n"}
      dictionaryOut append
        """    a
          |  }
          |""".stripMargin
    }
    dictionaryOut append "}\n"
  }
  
  private def renderPhraseConstantDeclarations(
    languageVariantKeys:Traversable[String],
    languageInvariantKeys:Traversable[String],
    technicalKeys:Traversable[String],
    out:Appendable
  ) {
    val languageVariantSet = languageVariantKeys.toSet
    val languageInvariantSet = languageInvariantKeys.toSet
    (((languageVariantKeys ++ languageInvariantKeys ++ technicalKeys) toVector) sorted) foreach {key =>
      out append s"  case object $key extends "
      if(languageVariantSet contains key) out append s"LanguageVariant {def apply[A<:HTMLAppend[A]](append:A):A = Dictionary.append$key(append)}"
      else if(languageInvariantSet contains key) out append s"LanguageInvariant {def apply[A<:HTMLAppend[A]](append:A):A = Dictionary.append$key(append)}"
      else out append s"Technical {def apply[A<:HTMLAppend[A]](append:A):A = Dictionary.TechnicalPhrases.append$key(append)}"
      out append '\n'
    }
  }
}
