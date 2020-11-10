package org.motechproject.mots.service;

import org.motechproject.mots.dto.AutomatedReportSettingsDto;
import org.motechproject.mots.exception.AutomatedReportException;
import org.motechproject.mots.scheduler.BaseJob;
import org.motechproject.mots.scheduler.SendReportJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

  @Autowired
  private SchedulerFactoryBean schedulerFactoryBean;

  @Autowired
  private SendReportJob sendReportJob;

  public void updateJob(AutomatedReportSettingsDto reportSettingsDto) {
    if (reportSettingsDto.getEnabled()) {
      addJob(reportSettingsDto.getJobName());
    } else {
      deleteJob(reportSettingsDto.getJobName());
    }
  }

  public void addJob(String jobName) {
    BaseJob job = jobFactory(jobName);
    JobDetail jobDetail = job.getJobDetail();
    Trigger trigger = job.getTrigger();
    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    try {
      if (scheduler.checkExists(trigger.getJobKey())) {
        schedulerFactoryBean.getScheduler().deleteJob(trigger.getJobKey());
      }
      schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      throw new AutomatedReportException(e.getMessage(), e);
    }
  }

  public void deleteJob(String jobName) {
    BaseJob job = jobFactory(jobName);
    Trigger trigger = job.getTrigger();
    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    try {
      if (scheduler.checkExists(trigger.getJobKey())) {
        schedulerFactoryBean.getScheduler().deleteJob(trigger.getJobKey());
      }
    } catch (SchedulerException e) {
      throw new AutomatedReportException(e.getMessage(), e);
    }
  }

  private BaseJob jobFactory(String jobName) {
    // Change to switch when more job will be available
    if (SendReportJob.NAME.equals(jobName)) {
      return sendReportJob;
    }
    return null;
  }
}
