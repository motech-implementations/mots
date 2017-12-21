package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;

public class ChoiceDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private Integer ivrPressedKey;

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
