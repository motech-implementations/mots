package org.motechproject.mots.scheduler;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.motechproject.mots.domain.enums.EmailSchedulePeriod;
import org.motechproject.mots.repository.AutomatedReportSettingsRepository;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AutomatedReportJob extends BaseJob {

  @Autowired
  private AutomatedReportSettingsRepository automatedReportSettingsRepository;

  @Override
  public Trigger getTrigger() {
    AutomatedReportSettings settings = automatedReportSettingsRepository
        .findOneByJobName(getFullName());
    return TriggerBuilder
        .newTrigger()
        .withDescription(getDescription())
        .forJob(getJobDetail())
        .withIdentity(getFullName())
        .withSchedule(getSchedule(settings))
        .startAt(getStartDate(settings))
        .build();
  }

  private CronScheduleBuilder getSchedule(AutomatedReportSettings settings) {
    EmailSchedulePeriod period = settings.getPeriod();
    Date date = settings.getStartDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    switch (period) {
      case DAILY:
        return CronScheduleBuilder.dailyAtHourAndMinute(date.getHours(), date.getMinutes());
      case WEEKLY:
        return CronScheduleBuilder.weeklyOnDayAndHourAndMinute(
            calendar.get(Calendar.DAY_OF_WEEK), date.getHours(), date.getMinutes());
      case MONTHLY:
        return CronScheduleBuilder.monthlyOnDayAndHourAndMinute(
            calendar.get(Calendar.DAY_OF_MONTH), date.getHours(), date.getMinutes());
      default:
        return null;
    }
  }

  private Date getStartDate(AutomatedReportSettings settings) {
    if (settings == null || settings.getStartDate() == null) {
      ZonedDateTime date = ZonedDateTime.now().plus(settings.getPeriod().getPeriod());
      return java.sql.Timestamp.valueOf(date.toLocalDateTime());
    } else {
      return settings.getStartDate();
    }
  }
}
