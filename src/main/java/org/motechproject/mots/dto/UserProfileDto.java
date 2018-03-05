package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;

public class UserProfileDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
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
