package org.motechproject.mots.validate.constraintvalidators;

import static org.motechproject.mots.validate.ValidationUtils.isValidDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.validate.annotations.PastDate;

public class PastDateFormatValidator implements ConstraintValidator<PastDate, String> {

  @Override
  public void initialize(PastDate parameters) {
    // we don't need any passed parameters
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isEmpty(value)
        || isValidDateFormat(MotsConstants.SIMPLE_DATE_FORMAT, value)) {
      SimpleDateFormat sdf = new SimpleDateFormat(MotsConstants.SIMPLE_DATE_FORMAT);
      try {
        Date date = sdf.parse(value);
        return date.before(new Date());
      } catch (ParseException ex) {
        return true;
      }
    }
    return true;
  }
}
