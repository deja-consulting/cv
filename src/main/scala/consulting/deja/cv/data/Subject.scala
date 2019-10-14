package consulting.deja.cv.data

import consulting.deja.cv.data.Country.Germany
import consulting.deja.cv.io.HTMLAppendable
import consulting.deja.cv.template.Phrase.{GermanNationalityMale, GitHubIcon, LinkedInIcon, XingIcon}

/** Data on the subject of the CV, i.e. the original author of this code. */
object Subject {
  def baseCountry:Country = Germany
  def nationality:HTMLAppendable = GermanNationalityMale
  def profileOnGitHub:String = "github.com/Madoc"
  def profileOnLinkedIn:String = "linkedin.com/in/matthias-deja"
  def profileOnXing:String = "xing.com/profile/MatthiasJ_Deja"

  val socialLinks:Seq[SocialLink] = Seq(
    SocialLink(LinkedInIcon, profileOnLinkedIn),
    SocialLink(GitHubIcon, profileOnGitHub),
    SocialLink(XingIcon, profileOnXing)
  )
}
