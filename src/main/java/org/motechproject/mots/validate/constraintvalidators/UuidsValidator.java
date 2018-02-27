package org.motechproject.mots.validate.constraintvalidators;

import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.Uuids;

public class UuidsValidator implements ConstraintValidator<Uuids, Set<String>> {

  @Override
  public void initialize(Uuids parameters) {
  }

  @Override
  public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
    String uuids = "";
    for (String uuid : values) {
      if (!ValidationUtils.isValidUuidString(uuid)) {
        uuids = uuids.concat(uuid + ", ");
      }
    }
    String message = String.format(ValidationMessages.INVALID_IDS, uuids);
    context.disableDefaultConstraintViolation();
    ValidationUtils.addDefaultViolationMessage(context, message);
    return uuids.isEmpty();
  }
}
