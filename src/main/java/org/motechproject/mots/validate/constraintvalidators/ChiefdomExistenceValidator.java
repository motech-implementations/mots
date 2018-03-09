package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.ChiefdomExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class ChiefdomExistenceValidator
    implements ConstraintValidator<ChiefdomExistence, String> {

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Override
  public void initialize(ChiefdomExistence constraintAnnotation) {
  }

  @Override
  public boolean isValid(String chiefdomId, ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(chiefdomId) && ValidationUtils.isValidUuidString(chiefdomId)) {
      UUID chiefdom = UUID.fromString(chiefdomId);
      return chiefdomRepository.exists(chiefdom);
    }
    return true;
  }
}
