package org.motechproject.mots.validate;

import java.util.Optional;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ChwValidator extends AbstractValidator {

  private static final String ERROR_CODE = "errorCode";

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

    rejectIfEmpty(errors, CHW_ID, chw.getChwId(), ValidationMessages.EMPTY_CHW_ID);
    rejectIfEmpty(errors, FIRST_NAME, chw.getFirstName(), ValidationMessages.EMPTY_FIRST_NAME);
    rejectIfEmpty(errors, SECOND_NAME, chw.getSecondName(), ValidationMessages.EMPTY_SECOND_NAME);
    rejectIfNull(errors, GENDER, chw.getGender(), ValidationMessages.EMPTY_GENDER);
    rejectIfEmpty(errors, PHONE_NUMBER, chw.getPhoneNumber(),
        ValidationMessages.EMPTY_PHONE_NUMBER);
    rejectIfNull(errors, COMMUNITY, chw.getCommunity(), ValidationMessages.EMPTY_COMMUNITY);
    rejectIfNull(errors, PREFERRED_LANGUAGE, chw.getPreferredLanguage(),
        ValidationMessages.EMPTY_PREFERRED_LANGUAGE);

    Optional<CommunityHealthWorker> existingChw = repository.findByChwId(chw.getChwId());

    existingChw.ifPresent(healthWorker -> {
      if (!healthWorker.getId().equals(chw.getId())) {
        errors.rejectValue("chwId", ERROR_CODE, "CHW with this CHW Id already exists");
      }
    });

    existingChw = repository.findByPhoneNumber(chw.getPhoneNumber());

    existingChw.ifPresent(healthWorker -> {
      if (!healthWorker.getId().equals(chw.getId())) {
        errors.rejectValue("phoneNumber", ERROR_CODE,
            "CHW with this Phone Number already exists");
      }
    });
  }
}
