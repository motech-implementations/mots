package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.ChiefdomExistence;
import org.motechproject.mots.validate.annotations.FacilityType;
import org.motechproject.mots.validate.annotations.FacilityUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@FacilityUniqueness
public class FacilityCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessages.EMPTY_LOCATION_NAME)
  private String name;

  @Setter
  @Getter
  @ChiefdomExistence
  @Uuid
  @NotEmpty(message = ValidationMessages.EMPTY)
  private String chiefdomId;

  @Setter
  @Getter
  @FacilityType
  @NotEmpty(message = ValidationMessages.EMPTY)
  private String facilityType;

  @Setter
  @Getter
  @NotEmpty(message = ValidationMessages.EMPTY)
  private String facilityId;

}
