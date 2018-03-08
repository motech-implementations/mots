package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.Uuid;

public class CommunityCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY)
  private String name;

  @Setter
  @Getter
  @FacilityExistence
  @Uuid
  @NotEmpty(message = ValidationMessages.EMPTY)
  private String facilityId;
}
