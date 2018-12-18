package org.motechproject.mots.validate.constraintvalidators;

import static org.motechproject.mots.validate.ValidationUtils.isValidDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.validate.annotations.DateTimeFormat;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value)
        || isValidDateFormat(MotsConstants.DATE_TIME_PATTERN, value);
  }

  @Override
  public void initialize(DateTimeFormat parameters) {
    // we don't need any passed parameters
  }
}
