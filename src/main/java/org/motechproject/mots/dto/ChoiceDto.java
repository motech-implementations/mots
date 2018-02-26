package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChoiceDto {

  @Uuid(message = ValidationMessages.INVALID_ID)
  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String ivrName;

  @Getter
  @Setter
  private Boolean isCorrect;

  @Getter
  @Setter
  private String description;
}
