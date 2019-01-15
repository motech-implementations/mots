package org.motechproject.mots.validate.constraintvalidators.incharge;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.repository.InchargeRepository;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.InchargeEmailUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class InchargeEmailValidator implements
    ConstraintValidator<InchargeEmailUniqueness, InchargeDto> {

  private static final String EMAIL = "email";

  @Autowired
  private InchargeRepository inchargeRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean isValid(InchargeDto inchargeDto, ConstraintValidatorContext context) {
    String email = inchargeDto.getEmail();
    if (StringUtils.isNotBlank(email)) {
      Optional<Incharge> existingIncharge = inchargeRepository.findOneByEmail(email);

      if (existingIncharge.isPresent()
          && (inchargeDto.getId() == null
          || !existingIncharge.get().getId().equals(UUID.fromString(inchargeDto.getId())))) {

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, EMAIL,
            String.format(ValidationMessages.INCHARGE_DUPLICATE_EMAIL,
                existingIncharge.get().getEmail()));
        return false;
      }
    }
    if (inchargeDto.isCreateUser()) {
      Optional<User> existingUser = userRepository.findOneByEmail(email);

      if (existingUser.isPresent()) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, EMAIL,
            String.format(ValidationMessages.USER_DUPLICATE_EMAIL,
                existingUser.get().getEmail()));
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(InchargeEmailUniqueness parameters) {
    // we don't need any passed parameters
  }
}
