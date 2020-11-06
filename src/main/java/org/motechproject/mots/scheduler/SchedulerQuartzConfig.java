package org.motechproject.mots.scheduler;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerQuartzConfig {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private QuartzProperties quartzProperties;

  /**
   * This method configures custom schedulerFactoryBean. It loads Quartz properties
   * from application.properties, and sets AutowiringBeanJobFactory which enables
   * autowiring of jobs. This makes possible to schedule Quartz jobs dynamically.
   * @return configured SchedulerFactoryBean for whole context
   */
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

    AutowiringBeanJobFactory jobFactory = new AutowiringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    schedulerFactoryBean.setJobFactory(jobFactory);
    Properties quartzApplicationProperties = new Properties();
    quartzApplicationProperties.putAll(quartzProperties.getProperties());
    schedulerFactoryBean.setQuartzProperties(quartzApplicationProperties);
    return schedulerFactoryBean;
  }
}
