package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.motechproject.mots.validate.annotations.SectorUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@SectorUniqueness
public class SectorCreationDto {

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
  @DistrictExistence
  @Uuid
  @NotEmpty(message = ValidationMessageConstants.EMPTY_DISTRICT_ID)
  private String districtId;

  @Getter
  @Setter
  private String ownerUsername;
}
