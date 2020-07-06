package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class DistrictExistenceValidator
    implements ConstraintValidator<DistrictExistence, String> {

  @Autowired
  private DistrictRepository districtRepository;

  @Override
  public void initialize(DistrictExistence constraintAnnotation) {
  }

  @Override
  public boolean isValid(String districtId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(districtId) && ValidationUtils.isValidUuidString(districtId)) {
      UUID district = UUID.fromString(districtId);
      return districtRepository.existsById(district);
    }
    return true;
  }
}
