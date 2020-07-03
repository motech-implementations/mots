package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.UserEmailUniqueness;
import org.motechproject.mots.validate.annotations.UsernameUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@UsernameUniqueness
@UserEmailUniqueness
public class UserDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_USERNAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String username;

  @Getter
  @Setter
  @Email(message = ValidationMessageConstants.INVALID_EMAIL)
  @JsonDeserialize(using = TrimTextDeserializer.class)
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
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String name;

  @Getter
  @Setter
  private Set<RoleDto> roles = new HashSet<>();

  @Getter
  @Setter
  @JsonIgnore
  private Boolean enabled = true;
}
