package org.motechproject.mots.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.validate.annotations.Uuid;

@RequiredArgsConstructor
@AllArgsConstructor
public class AutomatedReportSettingsDto {

  @Getter
  @Setter
  private String jobName;

  @Getter
  @Setter
  private Date startDate;

  @Getter
  @Setter
  private String period;

  @Getter
  @Setter
  private Boolean enabled = false;

  @Getter
  @Setter
  private String emails = "";

  @Getter
  @Setter
  private String messageSubject = "";

  @Getter
  @Setter
  private String messageBody = "";

  @Getter
  @Setter
  @Uuid
  private String templateId = "";
}
