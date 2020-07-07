package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class FacilityDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_LOCATION_NAME)
  private String name;

  @Getter
  @Setter
  private List<VillageDto> villages;

  @Getter
  @Setter
  private String inchargeFullName;

  @Getter
  @Setter
  private String inchargePhone;

  @Getter
  @Setter
  private String inchargeEmail;
}
