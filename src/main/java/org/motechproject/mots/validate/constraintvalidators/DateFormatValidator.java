package org.motechproject.mots.validate.constraintvalidators;

import static org.motechproject.mots.validate.ValidationUtils.isValidDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.validate.annotations.DateFormat;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value)
        || isValidDateFormat(MotsConstants.SIMPLE_DATE_FORMAT, value);
  }

  @Override
  public void initialize(DateFormat parameters) {
    // we don't need any passed parameters
  }
}
