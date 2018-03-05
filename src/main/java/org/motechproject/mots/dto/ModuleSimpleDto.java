package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class ModuleSimpleDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  @JsonInclude(Include.NON_NULL)
  private String name;

  @Getter
  @Setter
  @JsonInclude(Include.NON_NULL)
  private Boolean isStarted;
}
