package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.ChiefdomExistence;
import org.motechproject.mots.validate.annotations.DateFormat;
import org.motechproject.mots.validate.annotations.DateTimeFormat;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.motechproject.mots.validate.annotations.EndDateAfterStartDate;
import org.motechproject.mots.validate.annotations.ModulesExistence;
import org.motechproject.mots.validate.annotations.Uuid;
import org.motechproject.mots.validate.annotations.Uuids;

@EndDateAfterStartDate
public class DistrictAssignmentDto {

  @Getter
  @Setter
  @DistrictExistence
  @Uuid(message = ValidationMessages.INVALID_DISTRICT_ID)
  @NotEmpty(message = ValidationMessages.EMPTY_DISTRICT_ID)
  private String districtId;

  @Getter
  @Setter
  @ChiefdomExistence
  @Uuid(message = ValidationMessages.INVALID_CHIEFDOM_ID)
  private String chiefdomId;

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
