package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class CommunityCreationDto {

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
  @NotBlank(message = ValidationMessages.EMPTY_FACILITY_TYPE)
  private String facilityId;
}
