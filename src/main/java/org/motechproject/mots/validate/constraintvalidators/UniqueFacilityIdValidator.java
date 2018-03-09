package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.validate.annotations.UniqueFacilityId;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueFacilityIdValidator implements ConstraintValidator<UniqueFacilityId, String> {

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(String facilityId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(facilityId)) {
      Optional<Facility> existing = facilityRepository.findByFacilityId(facilityId);
      return !existing.isPresent();
    }

    return true;
  }

  @Override
  public void initialize(UniqueFacilityId parameters) {
    // we don't need any passed parameters
  }
}


