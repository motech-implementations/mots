package org.motechproject.mots.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class ChoiceDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotNull(message = ValidationMessageConstants.EMPTY_CHOICE_ID)
  private Integer choiceId;

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
