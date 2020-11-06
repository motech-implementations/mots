package org.motechproject.mots.scheduler;

import java.time.Instant;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendReportJob extends AutomatedReportJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(SendReportJob.class);

  public static final String NAME = "Automated report";

  private static final String DESCRIPTION =
      "This task is responsible for sending reports to stakeholders";

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFullName() {
    return NAME;
  }

  @Override
  protected void executeInternal(JobExecutionContext context) {
    LOGGER.info(getFullName() + " is being executed at: " + Instant.now());
  }
}
