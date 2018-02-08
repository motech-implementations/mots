package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.Uuid;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

  private static final String PATTERN =
      "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || ValidationUtils.matchPattern(PATTERN, value);
  }

  @Override
  public void initialize(Uuid parameters) {
    // we don't need any passed parameters
  }
}
