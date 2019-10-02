/** Parsed version of a translation key/value pair. */
final case class TranslationPhrase(key:String, language:String, instructions:Vector[String]) {
  def withInstructions(instrs:Traversable[String]):TranslationPhrase = copy(instructions = instructions ++ instrs)
}
