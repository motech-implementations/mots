package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.FacilityType;
import org.motechproject.mots.validate.annotations.FacilityUniqueness;
import org.motechproject.mots.validate.annotations.SectorExistence;
import org.motechproject.mots.validate.annotations.UniqueFacilityId;
import org.motechproject.mots.validate.annotations.Uuid;

@FacilityUniqueness
@UniqueFacilityId
public class FacilityCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessages.EMPTY_LOCATION_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String name;

  @Setter
  @Getter
  @SectorExistence
  @Uuid
  @NotEmpty(message = ValidationMessages.EMPTY_SECTOR_ID)
  private String sectorId;

  @Setter
  @Getter
  @FacilityType
  @NotEmpty(message = ValidationMessages.EMPTY_FACILITY_TYPE)
  private String facilityType;

  @Setter
  @Getter
  @NotEmpty(message = ValidationMessages.EMPTY_FACILITY_ID)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String facilityId;

  @Getter
  @Setter
  private String ownerUsername;
}
