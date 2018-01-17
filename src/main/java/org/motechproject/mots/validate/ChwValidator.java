package org.motechproject.mots.validate;

import java.util.Optional;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ChwValidator extends AbstractValidator {

  private static final String CHW_ID = "chwId";
  private static final String FIRST_NAME = "firstName";
  private static final String SECOND_NAME = "secondName";
  private static final String GENDER = "gender";
  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String COMMUNITY = "community";
  private static final String PREFERRED_LANGUAGE = "preferredLanguage";

  @Autowired
  private CommunityHealthWorkerRepository repository;

  @Override
  public boolean supports(Class<?> clazz) {
    return CommunityHealthWorker.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CommunityHealthWorker chw = (CommunityHealthWorker) target;

    rejectIfEmpty(errors, CHW_ID, chw.getChwId(), "CHW Id cannot be empty");
    rejectIfEmpty(errors, FIRST_NAME, chw.getFirstName(), "First Name cannot be empty");
    rejectIfEmpty(errors, SECOND_NAME, chw.getSecondName(), "Surname cannot be empty");
    rejectIfNull(errors, GENDER, chw.getGender(), "Gender cannot be empty");
    rejectIfEmpty(errors, PHONE_NUMBER, chw.getPhoneNumber(), "Phone Number cannot be empty");
    rejectIfNull(errors, COMMUNITY, chw.getCommunity(), "Community cannot be empty");
    rejectIfNull(errors, PREFERRED_LANGUAGE, chw.getPreferredLanguage(),
        "Preferred Language cannot be empty");

    Optional<CommunityHealthWorker> existingChw = repository.findByChwId(chw.getChwId());

    existingChw.ifPresent(healthWorker -> {
      if (!healthWorker.getId().equals(chw.getId())) {
        errors.rejectValue("chwId", "CHW with this CHW Id already exist");
      }
    });

    existingChw = repository.findByPhoneNumber(chw.getPhoneNumber());

    existingChw.ifPresent(healthWorker -> {
      if (!healthWorker.getId().equals(chw.getId())) {
        errors.rejectValue("phoneNumber", "CHW with this Phone Number already exist");
      }
    });
  }
}
