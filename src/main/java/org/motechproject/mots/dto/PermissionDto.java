package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class PermissionDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_PERMISSION_NAME)
  private String name;

  @Getter
  @Setter
  private String displayName;

  @Getter
  @Setter
  private Boolean readonly = false;
}
