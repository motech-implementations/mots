package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private static final String PATTERN = "^\\d*$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || ValidationUtils.matchPattern(PATTERN, value);
  }

  @Override
  public void initialize(PhoneNumber parameters) {
    // we don't need any passed parameters
  }
}
