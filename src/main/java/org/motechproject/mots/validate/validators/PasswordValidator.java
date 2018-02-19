package org.motechproject.mots.validate.validators;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordValidator {

  private PasswordValidator() {
  }

  public static boolean validateCurrentPassword(
      String currentPassword, String oldPasswordProvided) {
    return new BCryptPasswordEncoder().matches(oldPasswordProvided, currentPassword);
  }

}
