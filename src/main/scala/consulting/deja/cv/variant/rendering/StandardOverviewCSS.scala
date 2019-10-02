package consulting.deja.cv.variant.rendering

import java.nio.charset.Charset

import consulting.deja.cv.language.Language
import scalacss.DevDefaults._
import scalacss.internal.{Length, ValueT}

import scala.language.postfixOps

/** Provides the CSS for the [[consulting.deja.cv.variant.Variant.StandardOverview]] variant. */
case class StandardOverviewCSS(language:Language, charset:Charset) extends StyleSheet.Inline {
  import dsl._
  import FontFaces._

  lazy val renderedAsString:String = StandardOverviewPageCSS(this, language, charset).renderedAsString + render
  val fontCSSRefs:Seq[String] = Seq(assistant, openSans, openSansCondensed, oswald, robotoCondensed) map (_ url)

  val documentTitle:StyleA = style("document-title")(
    fonts.documentTitle,
    measures.documentTitleFontSize,
    textTransform.uppercase,
    color(colors.mainContrast)
  )

  val introProse:StyleA = style("intro-prose")(
    paddingRight(measures.thinPadding),
    paddingLeft(measures.thinPadding),
    unsafeChild("p")(marginTop(0.mm))
  )

  val personalDataContainer:StyleA = style("personal-data-container")(
    textAlign.right,
    fonts.personalData,
    measures.personalDataFontSize,
    unsafeChild("div")(paddingRight(measures.thinPadding))
  )

  val personalDataFinishLine:StyleA = style("personal-data-finish-line")(
    backgroundColor(colors.mainContrast),
    paddingTop(measures.thinPadding),
    paddingBottom(measures.thinPadding),
    marginTop(measures.thinPadding),
    marginBottom(measures.thinPadding),
    fonts.personalDataFinishLine,
    measures.personalDataFontSize,
    color(colors contrastToMainContrast),
    measures.personalDataFinishLineLetterSpacing
  )

  val personalDataPhoneNumber:StyleA = style("personal-data-phone-number")(
    paddingBottom(measures.personalDataGap)
  )

  val root:StyleA = style("root")(
    fonts.main,
    measures.mainFontSize,
    margin(0.mm)
  )

  val skillsExposeCategoryNameCell:StyleA = style("skills-expose-category-name-cell")(
    fonts.skillsExposeCategoryName,
    color(colors.boldFontColor),
    textAlign.right,
    width(measures.skillsExposeCategoryNameWidth),
    paddingRight(measures.thinPadding)
  )

  val skillsExposeSkillListCell:StyleA = style("skills-expose-skill-list-cell")(
    paddingLeft(measures.skillsExposeSkillListPadding)
  )

  val skillsExposeSubcategoryNameCell:StyleA = style("skills-expose-sub-category-name-cell")(
    fonts.skillsExposeSubcategoryName,
    color(colors.boldFontColor),
    textAlign.right,
    width(measures.skillsExposeSubcategoryNameWidth)
  )

  val skillsExposeTable:StyleA = style("skills-expose-table")(
    width(100 %%),
    marginTop(measures.skillsExposeTableTopMargin),
    unsafeChild("td")(paddingTop(0.mm), paddingBottom(0.mm), borderSpacing(0.mm))
  )

  val skillsExposeNewCategoryRow:StyleA = style("skills-expose-new-category")(
    unsafeChild("td")(paddingTop(measures.skillsExposeTableCategoryPadding))
  )

  val stationCoreSkills:StyleA = style("station-core-skills")(
    paddingLeft(measures.thinPadding),
    paddingRight(measures.thinPadding),
    marginBottom(measures.thinPadding),
    pageBreakBefore.avoid
  )

  val stationCoreSkillsHeading:StyleA = style("station-core-skills-heading")(
    float.left,
    width(17 %%),
    fonts.stationCoreSkillsHeading,
    measures.stationCoreSkillsHeadingFontSize,
    color(colors.boldFontColor),
    textAlign.right,
    paddingRight(measures.stationCoreSkillsHeadingPadding)
  )

  val stationCoreSkillsList:StyleA = style("station-core-skills-list")(
    fonts.stationCoreSkillsList,
    measures.stationCoreSkillsListFontSize
  )

  val stationDuration:StyleA = style("station-duration")(
    float.left,
    width(16 %%)
  )

  val stationHeading:StyleA = style("station-heading")(
    float.left,
    width(67.0 %%)
  )

  val stationListHeading:StyleA = style("station-list-heading")(
    fonts.stationListHeading,
    measures.stationListHeadingFontSize,
    color(colors.mainContrast),
    measures.stationListHeadingLetterSpacing,
    marginTop(measures.stationListHeadingTopMargin),
    marginBottom(measures.stationListHeadingBottomMargin),
    pageBreakAfter.avoid
  )

  val stationOverview:StyleA = style("station-overview")(
    paddingLeft(measures.thinPadding),
    paddingRight(measures.thinPadding),
    paddingTop(measures.stationOverviewPadding),
    paddingBottom(measures.stationOverviewPadding),
    pageBreakInside.avoid,
    unsafeChild("p")(margin(0.mm)),
    unsafeChild("ul")(margin(0.mm)),
    unsafeChild("li")(
      paddingLeft(measures.stationOverviewListItemPaddingAfterBullet),
      listStyleType:="none",
      position.relative,
      &.before(
        content:="'•'",
        position.absolute,
        left(measures.stationOverviewListItemPaddingBeforeBullet),
        top(-0.3.em),
        fontSize(150 %%)
      )
    )
  )

  val stationTitleRow:StyleA = style("station-title-row")(
    fonts.stationTitleRow,
    measures.stationTitleRowFontSize,
    backgroundColor(colors.lightColor),
    color(colors.lightContrast),
    paddingTop(measures.stationTitleRowTopPadding),
    paddingLeft(measures.thinPadding),
    paddingRight(measures.thinPadding),
    pageBreakAfter.avoid
  )

  val stationYear:StyleA = style("station-year")(
    textAlign.right
  )

  val subjectPhoto:StyleA = style("subject-photo")(
    float.left,
    height(measures.subjectPhotoHeight),
    paddingLeft(measures.thinPadding),
    paddingTop(measures.subjectPhotoTopPadding)
  )

  private[rendering] object colors {
    val boldFontColor:ValueT[ValueT.Color] = rgb(89, 89, 89)
    val contrastToMainContrast:ValueT[ValueT.Color] = rgb(242, 242, 242)
    val mainContrast:ValueT[ValueT.Color] = rgb(31, 56, 100)
    val lightColor:ValueT[ValueT.Color] = rgb(217, 225, 243)
    val lightContrast:ValueT[ValueT.Color] = rgb(59, 56, 56)
  }

  private[rendering] object fonts {
    lazy val documentTitle:StyleS = oswaldRegular
    lazy val main:StyleS = assistantRegular
    lazy val personalData:StyleS = openSansRegular
    lazy val personalDataFinishLine:StyleS = openSansBold
    lazy val skillsExposeCategoryName:StyleS = assistantBold
    lazy val skillsExposeSubcategoryName:StyleS = assistantItalic
    lazy val stationCoreSkillsHeading:StyleS = assistantBold
    lazy val stationCoreSkillsList:StyleS = assistantRegular
    lazy val stationListHeading:StyleS = robotoCondensedRegular
    lazy val stationTitleRow:StyleS = openSansCondensedBold

    private lazy val assistantBold:StyleS = mixin(fontFamily(assistant.face), fontWeight._700)
    private lazy val assistantItalic:StyleS = mixin(fontFamily(assistant.face), fontStyle.italic)
    private lazy val assistantRegular:StyleS = mixin(fontFamily(assistant.face), fontWeight._400)
    private lazy val openSansCondensedBold:StyleS = mixin(fontFamily(openSansCondensed.face), fontWeight._700)
    private lazy val openSansBold:StyleS = mixin(fontFamily(openSans.face), fontWeight._700)
    private lazy val openSansRegular:StyleS = mixin(fontFamily(openSans.face), fontWeight._400)
    private lazy val oswaldRegular:StyleS = mixin(fontFamily(oswald.face), fontWeight._400)
    private lazy val robotoCondensedRegular:StyleS = mixin(fontFamily(robotoCondensed.face), fontWeight._400)
  }

  private[rendering] object measures {
    lazy val documentTitleFontSize:StyleS = mixin(fontSize(biggestFontHeight))
    lazy val mainFontSize:StyleS = mixin(fontSize(defaultFontHeight))
    lazy val pageBorderThickness:Length[Double] = pageBorderThickness_mm.mm
    lazy val pageFooterMargin:Length[Double] = 10.0.mm
    lazy val pageFooterPadding:Length[Double] = 7.5.mm
    lazy val pageFooterSideWidth:Length[Double] = 50.0.mm
    lazy val pageHeaderMargin:Length[Double] = shortLength
    lazy val pageMargins:StyleS = mixin(
      marginBottom(20.mm),
      marginLeft(25.mm),
      marginRight(25.mm),
      marginTop((22+pageBorderThickness_mm).mm)
    )
    lazy val personalDataFontSize:StyleS = mixin(fontSize(defaultFontHeight))
    lazy val personalDataGap:Length[Double] = biggestFontHeight
    lazy val personalDataFinishLineLetterSpacing:StyleS = mixin(letterSpacing(baseUnit/1.5))
    lazy val skillsExposeCategoryNameWidth:Length[Double] = baseUnit*83
    lazy val skillsExposeSkillListPadding:Length[Double] = defaultFontHeight
    lazy val skillsExposeSubcategoryNameWidth:Length[Double] = baseUnit*135
    lazy val skillsExposeTableCategoryPadding:Length[Double] = baseUnit*4
    lazy val skillsExposeTableTopMargin:Length[Double] = -defaultFontHeight
    lazy val stationCoreSkillsHeadingFontSize:StyleS = mixin(fontSize(slightlySmallerFontHeight))
    lazy val stationCoreSkillsHeadingPadding:Length[Double] = defaultFontHeight
    lazy val stationCoreSkillsListFontSize:StyleS = mixin(fontSize(slightlySmallerFontHeight))
    lazy val stationListHeadingFontSize:StyleS = mixin(fontSize(biggerFontHeight))
    lazy val stationListHeadingLetterSpacing:StyleS = mixin(letterSpacing(baseUnit))
    lazy val stationListHeadingBottomMargin:Length[Double] = shortLength
    lazy val stationListHeadingTopMargin:Length[Double] = defaultFontHeight
    lazy val stationOverviewListItemPaddingAfterBullet:Length[Double] = 0.0.mm
    lazy val stationOverviewListItemPaddingBeforeBullet:Length[Double] = baseUnit*(-17)
    lazy val stationOverviewPadding:Length[Double] = shortLength
    lazy val stationTitleRowFontSize:StyleS = mixin(fontSize(defaultFontHeight))
    lazy val stationTitleRowTopPadding:Length[Double] = baseUnit
    lazy val subjectPhotoHeight:Length[Double] = 4.5.cm
    lazy val subjectPhotoTopPadding:Length[Double] = 0.25.mm
    lazy val thinPadding:Length[Double] = baseUnit*5

    private lazy val baseUnit:Length[Double] = baseUnit_mm.mm
    private lazy val baseUnit_mm:Double = 0.2917
    private lazy val biggerFontHeight:Length[Double] = baseUnit*19
    private lazy val biggestFontHeight:Length[Double] = baseUnit*29
    private lazy val defaultFontHeight:Length[Double] = baseUnit*12
    private lazy val shortLength:Length[Double] = baseUnit*3
    private lazy val pageBorderThickness_mm:Double = baseUnit_mm*8
    private lazy val slightlySmallerFontHeight:Length[Double] = baseUnit*11
  }
}
