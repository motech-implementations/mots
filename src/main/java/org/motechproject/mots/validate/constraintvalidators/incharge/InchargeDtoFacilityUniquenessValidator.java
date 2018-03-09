package org.motechproject.mots.validate.constraintvalidators.incharge;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.repository.InchargeRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.AssignFacilityToInchargeUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class InchargeDtoFacilityUniquenessValidator implements
    ConstraintValidator<AssignFacilityToInchargeUniqueness, InchargeDto> {

  private static final String FACILITY_ID = "facilityId";

  @Autowired
  private InchargeRepository inchargeRepository;

  @Override
  public boolean isValid(InchargeDto inchargeDto, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(inchargeDto.getFacilityId())
        && ValidationUtils.isValidUuidString(inchargeDto.getFacilityId())) {
      UUID facilityId = UUID.fromString(inchargeDto.getFacilityId());
      Optional<Incharge> existingIncharge = inchargeRepository.findByFacilityId(facilityId);

      if (existingIncharge.isPresent()
          && !StringUtils.equals(inchargeDto.getId(), existingIncharge.get().getId().toString())) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, FACILITY_ID);
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(AssignFacilityToInchargeUniqueness parameters) {
    // we don't need any passed parameters
  }
}
