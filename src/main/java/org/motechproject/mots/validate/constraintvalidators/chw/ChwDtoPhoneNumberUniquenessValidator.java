package org.motechproject.mots.validate.constraintvalidators.chw;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class ChwDtoPhoneNumberUniquenessValidator implements
    ConstraintValidator<PhoneNumberUniqueness, CommunityHealthWorkerDto> {

  private static final String PHONE_NUMBER = "phoneNumber";

  @Autowired
  private CommunityHealthWorkerRepository chwRepository;

  @Override
  public boolean isValid(CommunityHealthWorkerDto chwDto, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(chwDto.getPhoneNumber())) {
      Optional<CommunityHealthWorker> existingChw = chwRepository.findByPhoneNumber(
          chwDto.getPhoneNumber());

      if (existingChw.isPresent()
          && !StringUtils.equals(chwDto.getId(), existingChw.get().getId().toString())) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, PHONE_NUMBER);
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(PhoneNumberUniqueness parameters) {
    // we don't need any passed parameters
  }
}
