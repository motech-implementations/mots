package org.motechproject.mots.validate.constraintvalidators;

import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.Uuids;

public class UuidsValidator implements ConstraintValidator<Uuids, Set<String>> {

  @Override
  public void initialize(Uuids parameters) {
  }

  @Override
  public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
    StringBuilder uuids = new StringBuilder("");
    String delimiter = "";

    for (String uuid : values) {
      if (!ValidationUtils.isValidUuidString(uuid)) {
        uuids.append(delimiter).append(uuid);
        delimiter = ", ";
      }
    }
    String message = String.format(ValidationMessageConstants.INVALID_IDS, uuids.toString());
    context.disableDefaultConstraintViolation();
    ValidationUtils.addDefaultViolationMessage(context, message);
    return uuids.toString().isEmpty();
  }
}
