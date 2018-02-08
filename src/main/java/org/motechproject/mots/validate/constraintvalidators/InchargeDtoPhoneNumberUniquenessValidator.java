package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.repository.InchargeRepository;
import org.motechproject.mots.validate.annotations.incharge.InchargeDtoPhoneNumberUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class InchargeDtoPhoneNumberUniquenessValidator implements
    ConstraintValidator<InchargeDtoPhoneNumberUniqueness, InchargeDto> {

  @Autowired
  private InchargeRepository inchargeRepository;

  @Override
  public boolean isValid(InchargeDto inchargeDto, ConstraintValidatorContext context) {
    boolean result = true;

    Optional<Incharge> existingIncharge = inchargeRepository.findByPhoneNumber(
        inchargeDto.getPhoneNumber());
    UUID id = UUID.fromString(inchargeDto.getId());

    if (existingIncharge.isPresent()) {
      if (!existingIncharge.get().getId().equals(id)) {
        result = false;
      }
    }

    return result;
  }

  @Override
  public void initialize(InchargeDtoPhoneNumberUniqueness parameters) {
    // we don't need any passed parameters
  }
}
