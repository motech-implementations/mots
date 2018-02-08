package org.motechproject.mots.validate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class AbstractValidator implements Validator {

  protected void rejectIfEmpty(Errors errors, String field, String value, String errorMessage) {
    if (StringUtils.isBlank(value)) {
      errors.rejectValue(field, "", errorMessage);
    }
  }

  protected void rejectIfNull(Errors errors, String field, Object value, String errorMessage) {
    if (value == null) {
      errors.rejectValue(field, "", errorMessage);
    }
  }
}
