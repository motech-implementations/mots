package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.NewPasswordsMatch;
import org.motechproject.mots.validate.annotations.Uuid;

@NewPasswordsMatch
public class UserProfileDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String name;

  @Getter
  @Setter
  @Email(message = ValidationMessages.INVALID_EMAIL)
  @JsonDeserialize(using = TrimTextDeserializer.class)
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
