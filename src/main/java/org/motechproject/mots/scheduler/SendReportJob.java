package org.motechproject.mots.scheduler;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.activation.DataSource;
import org.motechproject.mots.domain.AutomatedReportSettings;
import org.motechproject.mots.exception.AutomatedReportException;
import org.motechproject.mots.repository.AutomatedReportSettingsRepository;
import org.motechproject.mots.service.JasperReportsExportService;
import org.motechproject.mots.service.MailService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendReportJob extends AutomatedReportJob {

  @Autowired
  private MailService mailService;

  @Autowired
  private JasperReportsExportService jasperReportsExportService;

  @Autowired
  private AutomatedReportSettingsRepository automatedReportSettingsRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(SendReportJob.class);

  public static final String NAME = "Automated report";

  public static final String REPORT_NAME = "MonitoringReport_";

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
    // replace line below after report development. Report for test purpose
    AutomatedReportSettings settings = automatedReportSettingsRepository.findOneByJobName(NAME);
    if (settings == null) {
      throw new AutomatedReportException("There is no report with such name");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    String date = ZonedDateTime.now().format(formatter);
    String fileName = REPORT_NAME + date + ".pdf";
    DataSource report = jasperReportsExportService.generatePdfReport("User Log");
    mailService.sentToMultipleAddress(report, settings, fileName);
  }
}
