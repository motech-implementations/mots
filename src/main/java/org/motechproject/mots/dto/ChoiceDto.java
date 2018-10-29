package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChoiceDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  private String ivrName;

  @Getter
  @Setter
  private String type;

  @Getter
  @Setter
  private String description;
}
