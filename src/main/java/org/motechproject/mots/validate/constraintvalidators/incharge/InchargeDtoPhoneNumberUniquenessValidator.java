package org.motechproject.mots.validate.constraintvalidators.incharge;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.repository.InchargeRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class InchargeDtoPhoneNumberUniquenessValidator implements
    ConstraintValidator<PhoneNumberUniqueness, InchargeDto> {

  private static final String PHONE_NUMBER = "phoneNumber";

  @Autowired
  private InchargeRepository inchargeRepository;

  @Override
  public boolean isValid(InchargeDto inchargeDto, ConstraintValidatorContext context) {
    boolean isValid = true;
    if (StringUtils.isNotBlank(inchargeDto.getPhoneNumber())) {
      Optional<Incharge> existingIncharge = inchargeRepository.findByPhoneNumber(
          inchargeDto.getPhoneNumber());

      if (existingIncharge.isPresent()
          && !StringUtils.equals(inchargeDto.getId(), existingIncharge.get().getId().toString())) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, PHONE_NUMBER);
        isValid = false;
      }
    }
    return isValid;
  }

  @Override
  public void initialize(PhoneNumberUniqueness parameters) {
    // we don't need any passed parameters
  }
}
