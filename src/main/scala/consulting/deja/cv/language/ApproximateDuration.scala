package consulting.deja.cv.language

import java.time.Duration

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase._

final case class ApproximateDuration(duration:Duration) extends HTMLAppendable {
  import ApproximateDuration._

  def apply[A<:HTMLAppend[A]](append:A):A =
    if(duration.isNegative) ApproximateDuration(duration.negated)(append("-"))
    else duration.toDays match {
      case daysInYears if daysInYears>=11*DaysPerMonth =>
        appendUnit(daysInYears, DaysPerYear, TimeUnitYearSingular, TimeUnitYearPlural, append)
      case daysInMonths if daysInMonths>=22 =>
        appendUnit(daysInMonths, DaysPerMonth, TimeUnitMonthSingular, TimeUnitMonthPlural, append)
      case daysInWeeks if daysInWeeks>=6 =>
        appendUnit(daysInWeeks, DaysPerWeek, TimeUnitWeekSingular, TimeUnitWeekPlural, append)
      case justDays =>
        appendUnit(justDays, 1, TimeUnitDaySingular, TimeUnitDayPlural, append)
    }

  private def appendUnit[A<:HTMLAppend[A]](days:Long, daysPerUnit:Int, singular:HTMLAppendable, plural:HTMLAppendable, append:A):A =
    math.round(days.toDouble/daysPerUnit) match {
      case 1 => append("1 ")(singular)
      case pluralNumber => append(s"$pluralNumber ")(plural)
    }
}
object ApproximateDuration {
  private val DaysPerMonth = 30
  private val DaysPerWeek = 7
  private val DaysPerYear = 365
}
