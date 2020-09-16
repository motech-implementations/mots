package org.motechproject.mots.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChwInfoDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_CHW_ID)
  private String chwId;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_CHW_NAME)
  private String chwName;
}
