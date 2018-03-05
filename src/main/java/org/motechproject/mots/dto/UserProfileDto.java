package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.NewPasswordsMatch;
import org.motechproject.mots.validate.annotations.Uuid;

@NewPasswordsMatch
public class UserProfileDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  @Email(message = ValidationMessages.INVALID_EMAIL)
  private String email;

  @Getter
  @Setter
  private String password;

  @Getter
  @Setter
  private String newPassword;

  @Getter
  @Setter
  private String confirmNewPassword;
}
