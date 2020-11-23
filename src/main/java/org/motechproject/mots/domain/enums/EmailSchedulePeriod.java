package org.motechproject.mots.domain.enums;

import java.time.Period;
import java.util.Locale;

public enum EmailSchedulePeriod {
  DAILY(Period.ofDays(1)),
  WEEKLY(Period.ofWeeks(1)),
  MONTHLY(Period.ofMonths(1));

  private Period period;

  EmailSchedulePeriod(Period period) {
    this.period = period;
  }

  public Period getPeriod() {
    return period;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   * @return display name of ChoiceType
   */
  public static EmailSchedulePeriod getByDisplayName(String name) {
    switch (name.toUpperCase(Locale.ENGLISH)) {
      case "DAILY":
        return DAILY;
      case "WEEKLY":
        return WEEKLY;
      case "MONTHLY":
        return MONTHLY;
      default:
        return null;
    }
  }
}
