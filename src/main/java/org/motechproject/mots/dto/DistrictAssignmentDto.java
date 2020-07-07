package org.motechproject.mots.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.SectorExistence;
import org.motechproject.mots.validate.annotations.Uuid;

public class DistrictAssignmentDto extends BulkAssignmentDto {

  @Getter
  @Setter
  @DistrictExistence
  @Uuid(message = ValidationMessageConstants.INVALID_DISTRICT_ID)
  @NotEmpty(message = ValidationMessageConstants.EMPTY_DISTRICT_ID)
  private String districtId;

  @Getter
  @Setter
  @SectorExistence
  @Uuid(message = ValidationMessageConstants.INVALID_SECTOR_ID)
  private String sectorId;

  @Getter
  @Setter
  @FacilityExistence
  @Uuid(message = ValidationMessageConstants.INVALID_FACILITY_ID)
  private String facilityId;
}
