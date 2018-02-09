package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class FacilityExistenceValidator implements ConstraintValidator<FacilityExistence, String> {

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(String facilityId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(facilityId) && ValidationUtils.isValidUuidString(facilityId)) {
      UUID facility = UUID.fromString(facilityId);
      if (!facilityRepository.exists(facility)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(FacilityExistence parameters) {
    // we don't need any passed parameters
  }
}
