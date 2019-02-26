package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.DateFormat;
import org.motechproject.mots.validate.annotations.DateTimeFormat;
import org.motechproject.mots.validate.annotations.EndDateAfterStartDate;
import org.motechproject.mots.validate.annotations.ModulesExistence;
import org.motechproject.mots.validate.annotations.Uuids;

@EndDateAfterStartDate
public class BulkAssignmentDto {

  @Getter
  @Setter
  @ModulesExistence
  @Uuids
  @NotEmpty(message = ValidationMessages.EMPTY_MODULES)
  private Set<String> modules;

  @Getter
  @Setter
  @DateFormat
  @NotEmpty(message = ValidationMessages.EMPTY_START_DATE)
  private String startDate;

  @Getter
  @Setter
  @DateFormat
  @NotEmpty(message = ValidationMessages.EMPTY_END_DATE)
  private String endDate;

  @Getter
  @Setter
  @DateTimeFormat
  private String notificationTime;
}
