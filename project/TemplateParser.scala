import java.io.File

import scala.annotation.tailrec
import scala.language._
import scala.xml._

/** Parses a given base directory recursively for template files. */
object TemplateParser {
  val AttributeKeyPrefix = "data-cv-"

  val AttributeKeyDictionary = s"${AttributeKeyPrefix}dictionary"
  val AttributeKeyTemplateExtract = s"${AttributeKeyPrefix}template-extract"
  val AttributeKeyTemplateName = s"${AttributeKeyPrefix}template-name"
  val AttributeKeyTemplateParams = s"${AttributeKeyPrefix}template-params"

  def apply(baseFile:File):Vector[TemplateGroup] =
    if(baseFile isDirectory) (baseFile listFiles).foldLeft(Vector[TemplateGroup]()) {(groups, subFile) => recurseOn(subFile, "", groups)}
    else Vector()

  private def recurseOn(file:File, localFilePrefix:String, templateGroups:Vector[TemplateGroup]):Vector[TemplateGroup] =
    if(isTemplateFile(file)) {
      val localFilePath = if(localFilePrefix isEmpty) file getName else s"$localFilePrefix/${file getName}"
      templateGroups :+ parseDocument(XML loadFile file, localFilePath)
    }
    else if(file isDirectory) {
      val newPrefix = if(localFilePrefix isEmpty) file getName else s"$localFilePrefix/${file getName}"
      (file listFiles).foldLeft(templateGroups) {(groups, f) => recurseOn(f, newPrefix, groups)}
    }
    else templateGroups

  private def parseDocument(elem:Elem, localFilePath:String):TemplateGroup = {
    val (templates, phrases) = parseOutsideOfTemplate(elem, Vector(), Vector())
    phrases groupBy (p => (p language, p key)) foreach {
      case ((_,_), Seq(_)) => // just one phrase per language/key, way to go
      case ((lang, key), _) => sys error s"Duplicate definition of phrase '$key' in language '$lang' in: $localFilePath"
    }
    TemplateGroup(localFilePath replaceAll ("""\.([^\.]*)$""", ""), templates groupBy (_.name) mapValues {
      case Seq(justOneTemplate) => justOneTemplate
      case several => sys error s"Duplicate definition of template with name: ${(several head) name}"
    }, phrases)
  }

  @tailrec def parseOutsideOfTemplate(nodes:Traversable[Node], templates:Vector[Template], phrases:Vector[TranslationPhrase]):(Vector[Template], Vector[TranslationPhrase]) =
    nodes headOption match {
      case None => (templates, phrases)
      case Some(elem:Elem) if isTemplateElem(elem) => parseOutsideOfTemplate(nodes tail, parseTemplate(elem, templates), phrases)
      case Some(elem:Elem) if isDictionaryElem(elem) => parseOutsideOfTemplate(nodes tail, templates, parseDictionary(elem, phrases))
      case Some(elem:Elem) => parseOutsideOfTemplate((elem child) ++ (nodes tail), templates, phrases)
      case Some(Group(contents)) => parseOutsideOfTemplate(contents ++ (nodes tail), templates, phrases)
      case _ => parseOutsideOfTemplate(nodes tail, templates, phrases)
    }

  private def parseTemplate(templateElem:Elem, templatesSoFar:Vector[Template]):Vector[Template] =
    TemplateExtractor(templateElem, templatesSoFar)

  private def parseDictionary(dictElem:Elem, phrasesSoFar:Vector[TranslationPhrase]):Vector[TranslationPhrase] = {
    val language = getLanguageFromDictionaryElem(dictElem)
    var currentKey:Option[String] = None
    var result = phrasesSoFar
    @tailrec def recurse(nodes:Seq[Node]) {nodes match {
      case Seq() => // done
      case Seq(elem:Elem, rest@_*) if (currentKey isEmpty) && isPhraseKeyElem(elem) =>
        currentKey = Some(parsePhraseKey(elem))
        recurse(rest)
      case Seq(elem:Elem, rest@_*) if (currentKey nonEmpty) && isPhraseElem(elem) =>
        result :+= parsePhrase(elem, currentKey get, language)
        currentKey = None
        recurse(rest)
      case _ => recurse(nodes tail)
    }}
    recurse(dictElem child)
    result
  }

  private def parsePhraseKey(phraseKeyElem:Elem):String =
    ((phraseKeyElem text) filter (Character isJavaIdentifierPart)) trim

  private def parsePhrase(phraseElem:Elem, phraseKey:String, language:String):TranslationPhrase =
    TranslationPhraseExtractor(phraseElem, TranslationPhrase(phraseKey, language, Vector empty))

  private def getLanguageFromDictionaryElem(elem:Elem):String = (((elem attribute AttributeKeyDictionary) get) text) trim
  private def getNameFromTemplateElem(elem:Elem):String = (((elem attribute AttributeKeyTemplateName) get) text) trim
  private def getParamsFromTemplateElem(elem:Elem):Seq[String] = elem attribute AttributeKeyTemplateParams match {
    case None => Seq()
    case Some(attValue) => ((attValue text) split ',') map (_.filter(Character.isJavaIdentifierPart)) filterNot (_ isEmpty)
  }

  private def isDictionaryElem(elem:Elem):Boolean = (elem attribute AttributeKeyDictionary) isDefined
  private def isPhraseElem(elem:Elem):Boolean = (elem label) == "dd"
  private def isPhraseKeyElem(elem:Elem):Boolean = (elem label) == "dt"
  private def isTemplateElem(elem:Elem):Boolean = (elem attribute AttributeKeyTemplateName) isDefined
  private def isTemplateExtractChild(elem:Elem):Boolean = (elem attribute AttributeKeyTemplateExtract) exists (_.text.trim == "child")
  private def isTemplateFile(file:File):Boolean = (file isFile) && ((file getName) endsWith ".html")

  private object TemplateExtractor extends Extractor[Vector[Template]] {
    def isExtractChild(extractElem:Elem):Boolean = isTemplateExtractChild(extractElem)
    def maybeExtractTemplate(templateElem:Elem, extractElem:Elem, state:Vector[Template]):Option[Vector[Template]] =
      if((templateElem ne extractElem) && isTemplateElem(templateElem)) Some(parseTemplate(templateElem, state))
      else None
    def result(extractElem:Elem, state:Vector[Template], instructions:Vector[String]) =
      state :+ Template(getNameFromTemplateElem(extractElem), getParamsFromTemplateElem(extractElem), instructions)
  }

  private object TranslationPhraseExtractor extends Extractor[TranslationPhrase] {
    def isExtractChild(extractElem:Elem):Boolean = true
    def maybeExtractTemplate(templateElem:Elem, extractElem:Elem, state:TranslationPhrase):Option[TranslationPhrase] = None
    def result(extractElem:Elem, state:TranslationPhrase, instructions:Vector[String]):TranslationPhrase =
      state withInstructions instructions
  }
}
