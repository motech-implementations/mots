package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.repository.SectorRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.SectorExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class SectorExistenceValidator
    implements ConstraintValidator<SectorExistence, String> {

  @Autowired
  private SectorRepository sectorRepository;

  @Override
  public void initialize(SectorExistence constraintAnnotation) {
  }

  @Override
  public boolean isValid(String sectorId, ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(sectorId) && ValidationUtils.isValidUuidString(sectorId)) {
      UUID sector = UUID.fromString(sectorId);
      return sectorRepository.exists(sector);
    }
    return true;
  }
}
