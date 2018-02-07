package org.motechproject.mots.validate.constraintvalidators;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

  private static final Pattern PATTERN = Pattern.compile(MotsConstants.UUID_PATTERN);

  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || PATTERN.matcher(value).matches();
  }

  public void initialize(Uuid parameters) { }
}
