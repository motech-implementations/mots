package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.ChiefdomExistence;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.Uuid;

public class DistrictAssignmentDto extends BulkAssignmentDto {

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
  @FacilityExistence
  @Uuid(message = ValidationMessages.INVALID_FACILITY_ID)
  private String facilityId;
}
