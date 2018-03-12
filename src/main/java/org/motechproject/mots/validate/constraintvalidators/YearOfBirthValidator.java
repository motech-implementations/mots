package org.motechproject.mots.validate.constraintvalidators;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.annotations.YearOfBirth;

public class YearOfBirthValidator implements ConstraintValidator<YearOfBirth, String> {

  private static final int MINIMAL_YEAR_OF_BIRTH = 1900;

  @Override
  public void initialize(YearOfBirth parameters) {
    // we don't need any passed parameters
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value != null) {
      if (StringUtils.isNumeric(value)) {

        int yearInt = Integer.valueOf(value);

        return yearInt >= MINIMAL_YEAR_OF_BIRTH && yearInt <= LocalDate.now().getYear() - 15;
      }
      return false;
    }
    return true;
  }
}
