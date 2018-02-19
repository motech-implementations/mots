package org.motechproject.mots.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChoiceDto {

  @Uuid(message = ValidationMessages.INVALID_ID)
  @Getter
  @Setter
  private String id;

  @Min(value = 0, message = ValidationMessages.NEGATIVE_IVR_PRESSED_KEY)
  @NotNull(message = ValidationMessages.NULL_IVR_PRESSED_KEY)
  @Getter
  @Setter
  private Integer ivrPressedKey;

  @Getter
  @Setter
  private String ivrName;

  @NotNull(message = ValidationMessages.NULL_IS_CORRECT)
  @Getter
  @Setter
  private Boolean isCorrect;

  @Getter
  @Setter
  private String description;
}
