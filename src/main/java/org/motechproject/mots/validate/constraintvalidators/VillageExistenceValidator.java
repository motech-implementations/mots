package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.repository.VillageRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.VillageExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class VillageExistenceValidator implements
    ConstraintValidator<VillageExistence, String> {

  @Autowired
  private VillageRepository villageRepository;

  @Override
  public boolean isValid(String villageId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(villageId) && ValidationUtils.isValidUuidString(villageId)) {
      UUID village = UUID.fromString(villageId);
      return villageRepository.exists(village);
    }
    return true;
  }

  @Override
  public void initialize(VillageExistence parameters) {
    // we don't need any passed parameters
  }
}
