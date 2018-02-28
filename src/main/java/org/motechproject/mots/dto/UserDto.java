package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.UsernameUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@UsernameUniqueness
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
  @Email(message = ValidationMessages.INVALID_EMAIL)
  private String email;

  @Getter
  @Setter
  @JsonInclude(Include.NON_NULL)
  private String password;

  @Getter
  @Setter
  @JsonInclude(Include.NON_NULL)
  private String passwordConfirm;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<RoleDto> roles = new HashSet<>();

  @Getter
  @Setter
  @JsonIgnore
  private Boolean enabled = true;
}
