package org.motechproject.mots.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class RoleDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_ROLE_NAME)
  private String name;

  @Getter
  @Setter
  private Boolean readonly = false;

  @Getter
  @Setter
  private Set<PermissionDto> permissions = new HashSet<>();
}
