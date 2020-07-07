package org.motechproject.mots.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class PermissionDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_PERMISSION_NAME)
  private String name;

  @Getter
  @Setter
  private String displayName;

  @Getter
  @Setter
  private Boolean readonly = false;
}
