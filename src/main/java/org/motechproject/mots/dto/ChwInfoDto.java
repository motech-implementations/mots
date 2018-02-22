package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChwInfoDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_CHW_ID)
  private String chwId;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FIRST_NAME)
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_SECOND_NAME)
  private String secondName;

  @Getter
  @Setter
  private String otherName;
}
