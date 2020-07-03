package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.Uuid;
import org.motechproject.mots.validate.annotations.VillageUniqueness;

@VillageUniqueness
public class VillageCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_LOCATION_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String name;

  @Setter
  @Getter
  @FacilityExistence
  @Uuid
  @NotEmpty(message = ValidationMessageConstants.EMPTY_FACILITY_ID)
  private String facilityId;

  @Getter
  @Setter
  private String ownerUsername;
}
