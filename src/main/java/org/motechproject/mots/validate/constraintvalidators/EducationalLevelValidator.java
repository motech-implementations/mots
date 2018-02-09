package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.annotations.EducationLevel;

public class EducationalLevelValidator implements ConstraintValidator<EducationLevel, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value)
        || org.motechproject.mots.domain.enums.EducationLevel.getByDisplayName(value) != null;
  }

  @Override
  public void initialize(EducationLevel parameters) {
    // we don't need any passed parameters
  }
}
