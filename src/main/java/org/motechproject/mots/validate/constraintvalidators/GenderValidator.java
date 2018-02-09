package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.annotations.Gender;

public class GenderValidator implements ConstraintValidator<Gender, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value)
        || org.motechproject.mots.domain.enums.Gender.getByDisplayName(value) != null;
  }

  @Override
  public void initialize(Gender parameters) {
    // we don't need any passed parameters
  }
}
