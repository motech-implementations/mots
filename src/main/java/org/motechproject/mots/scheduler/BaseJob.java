package org.motechproject.mots.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class BaseJob extends QuartzJobBean {

  public abstract String getDescription();

  public abstract String getFullName();

  /**
   * Default build JobDetail method. Uses full name as an identity.
   *
   * @return built JobDetails
   */
  public JobDetail getJobDetail() {
    return JobBuilder.newJob(getClass())
        .withIdentity(getFullName())
        .build();
  }

  /**
   * Default build Trigger method. Uses description, job details, full name, and schedule.
   * It will be used with scheduling jobs from Services.
   *
   * @return built JobDetails
   */
  public abstract Trigger getTrigger();

  /**
   * Default build initial Trigger method. Uses description, job details, full name, and schedule.
   * It is being used for initial job scheduling.
   *
   * @return built JobDetails
   */
  public Trigger getInitTrigger() {
    return getTrigger();
  }
}
