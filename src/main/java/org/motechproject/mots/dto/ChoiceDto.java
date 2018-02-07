package org.motechproject.mots.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ChoiceDto {

  @Getter
  @Setter
  private String id;

  @NotNull(message = "Choice IVR Pressed Key cannot be empty")
  @Getter
  @Setter
  private Integer ivrPressedKey;

  @Getter
  @Setter
  private String ivrName;

  @NotNull(message = "Choice Is Correct cannot be null")
  @Getter
  @Setter
  private Boolean isCorrect;

  @Getter
  @Setter
  private String description;
}
