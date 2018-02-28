package org.motechproject.mots.validate.constraintvalidators.user;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.UsernameUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameValidator implements
    ConstraintValidator<UsernameUniqueness, UserDto> {

  private static final String USERNAME = "username";

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
    String newUserUsername = userDto.getUsername();
    if (StringUtils.isNotBlank(newUserUsername)) {
      Optional<User> existingUser = userRepository.findOneByUsername(newUserUsername);

      if (existingUser.isPresent()
          && !existingUser.get().getId().equals(UUID.fromString(userDto.getId()))) {

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, USERNAME,
            String.format(ValidationMessages.NOT_UNIQUE_USERNAME,
                existingUser.get().getUsername()));
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(UsernameUniqueness parameters) {
    // we don't need any passed parameters
  }
}
