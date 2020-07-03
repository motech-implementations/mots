package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.dto.UserProfileDto;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.NewPasswordsMatch;

public class UserProfileDtoNewPasswordsMatchValidator implements
    ConstraintValidator<NewPasswordsMatch, UserProfileDto> {

  private static final String CONFIRM_NEW_PASSWORD = "confirmNewPassword";

  @Override
  public void initialize(NewPasswordsMatch parameters) {
  }

  @Override
  public boolean isValid(UserProfileDto userProfileDto, ConstraintValidatorContext context) {
    if (userProfileDto.getNewPassword() != null
        && userProfileDto.getConfirmNewPassword() != null
        && !passwordsMatch(userProfileDto)) {

      context.disableDefaultConstraintViolation();
      ValidationUtils.addDefaultViolationMessageToInnerField(context, CONFIRM_NEW_PASSWORD,
          ValidationMessageConstants.PASSWORDS_NOT_MATCH);
      return false;
    }
    return true;
  }

  private boolean passwordsMatch(UserProfileDto userProfileDto) {
    return userProfileDto.getNewPassword().equals(userProfileDto.getConfirmNewPassword());
  }
}
