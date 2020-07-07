package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.FacilityUniqueness;
import org.motechproject.mots.validate.annotations.SectorExistence;
import org.motechproject.mots.validate.annotations.Uuid;

@FacilityUniqueness
public class FacilityCreationDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessageConstants.EMPTY_LOCATION_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String name;

  @Setter
  @Getter
  @SectorExistence
  @Uuid
  @NotEmpty(message = ValidationMessageConstants.EMPTY_SECTOR_ID)
  private String sectorId;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String inchargeFullName;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String inchargePhone;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String inchargeEmail;

  @Getter
  @Setter
  private String ownerUsername;
}
