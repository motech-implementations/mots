package org.motechproject.mots.validate.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.validate.annotations.CallFlowElementType;

public class CallFlowElementTypeValidator implements
    ConstraintValidator<CallFlowElementType, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || checkIfEnumExists(value);
  }

  @Override
  public void initialize(CallFlowElementType parameters) {
    // we don't need any passed parameters
  }

  private boolean checkIfEnumExists(String enumValue) {
    try {
      org.motechproject.mots.domain.enums.CallFlowElementType.valueOf(enumValue);
      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
