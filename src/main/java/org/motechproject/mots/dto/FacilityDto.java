package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.FacilityType;
import org.motechproject.mots.validate.annotations.Uuid;

public class FacilityDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_LOCATION_NAME)
  private String name;

  @Getter
  @Setter
  @FacilityType
  @NotEmpty(message = ValidationMessages.EMPTY_FACILITY_TYPE)
  private String type;

  @Getter
  @Setter
  private List<CommunityDto> communities;

  @Getter
  @Setter
  private String inchargeId;
}
