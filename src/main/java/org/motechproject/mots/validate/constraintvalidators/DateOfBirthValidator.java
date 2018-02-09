package org.motechproject.mots.validate.constraintvalidators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.validate.annotations.DateOfBirth;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || isValidFormat(MotsConstants.SIMPLE_DATE_FORMAT, value);
  }

  @Override
  public void initialize(DateOfBirth parameters) {
    // we don't need any passed parameters
  }

  private boolean isValidFormat(String format, String value) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    try {
      Date date = sdf.parse(value);
      return value.equals(sdf.format(date));
    } catch (ParseException ex) {
      return false;
    }
  }
}
