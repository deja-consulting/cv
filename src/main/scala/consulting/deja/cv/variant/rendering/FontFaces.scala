package consulting.deja.cv.variant.rendering

import scalacss.DevDefaults.*
import scalacss.internal.FontFace

import java.net.URLEncoder

/** Independent `font-face` declaration for the other CSS renderers. Those will not be included in the generated CSS
 * though. The reason is because the proper font-face definitions stylesheets get included independently. */
private[rendering] object FontFaces extends StyleSheet.Inline:
  val assistant: FontDeclaration = googleFont("Assistant", "400", "700")
  val openSans: FontDeclaration = googleFont("Open Sans", "400", "400i", "700", "700i")
  val openSansCondensed: FontDeclaration = googleFont("Open Sans Condensed", "300", "300i", "700")
  val oswald: FontDeclaration = googleFont("Oswald", "400", "700")
  val robotoCondensed: FontDeclaration = googleFont("Roboto Condensed", "400", "400i", "700", "700i")

  final case class FontDeclaration(faceName: String, url: String):
    def face: FontFace[String] = fontFace(s"'$faceName'")(_.src(s"local('$faceName')"))
  private def googleFont(name: String, variants: String*): FontDeclaration = FontDeclaration(
    name,
    s"https://fonts.googleapis.com/css?family=${URLEncoder `encode` (name, "ISO-8859-1")}:${variants.mkString(",")}"
  )
