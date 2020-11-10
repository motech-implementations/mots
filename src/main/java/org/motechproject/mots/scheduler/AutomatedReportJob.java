package org.motechproject.mots.scheduler;

import java.util.Calendar;
import java.util.Date;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.motechproject.mots.repository.AutomatedReportSettingsRepository;
import org.quartz.SimpleScheduleBuilder;
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

  private SimpleScheduleBuilder getSchedule(AutomatedReportSettings settings) {
    return SimpleScheduleBuilder
        .simpleSchedule()
        .withIntervalInSeconds(getIntervalInSeconds(settings))
        .repeatForever();
  }

  private int getIntervalInSeconds(AutomatedReportSettings settings) {
    return settings.getIntervalInSeconds();
  }

  private Date getStartDate(AutomatedReportSettings settings) {
    if (settings == null || settings.getStartDate() == null) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, getIntervalInSeconds(settings));
      calendar.getTime();
      return calendar.getTime();
    } else {
      return settings.getStartDate();
    }
  }
}
