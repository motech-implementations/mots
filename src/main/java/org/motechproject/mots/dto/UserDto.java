package org.motechproject.mots.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class UserDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_USERNAME)
  private String username;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<RoleDto> roles = new HashSet<>();

  @Getter
  @Setter
  @NotNull(message = ValidationMessages.NULL_USER_ENABLED_IS_NULL)
  private Boolean enabled;
}
