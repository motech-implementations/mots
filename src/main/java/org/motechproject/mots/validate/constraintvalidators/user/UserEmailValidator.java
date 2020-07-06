package org.motechproject.mots.validate.constraintvalidators.user;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.UserEmailUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class UserEmailValidator implements
    ConstraintValidator<UserEmailUniqueness, UserDto> {

  private static final String EMAIL = "email";

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
    String email = userDto.getEmail();
    if (StringUtils.isNotBlank(email)) {
      Optional<User> existingUser = userRepository.findOneByEmail(email);

      if (existingUser.isPresent()
          && (userDto.getId() == null
          || !existingUser.get().getId().equals(UUID.fromString(userDto.getId())))) {

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, EMAIL,
            String.format(ValidationMessageConstants.USER_DUPLICATE_EMAIL,
                existingUser.get().getEmail()));
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(UserEmailUniqueness parameters) {
    // we don't need any passed parameters
  }
}
