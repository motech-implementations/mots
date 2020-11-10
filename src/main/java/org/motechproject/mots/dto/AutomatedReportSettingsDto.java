package org.motechproject.mots.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
  private Integer intervalInSeconds;

  @Getter
  @Setter
  private Boolean enabled = false;

  @Getter
  @Setter
  private String emails = "";
}
