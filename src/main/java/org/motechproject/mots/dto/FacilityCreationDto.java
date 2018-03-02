package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class FacilityCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_LOCATION_NAME)
  private String name;

  @Setter
  @Getter
  private String districtId;

  @Setter
  @Getter
  private String chiefdomId;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  @NotBlank(message = ValidationMessages.EMPTY_FACILITY_TYPE)
  private String facilityType;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  @NotBlank(message = ValidationMessages.EMPTY_FACILITY_ID)
  private String facilityId;

}
