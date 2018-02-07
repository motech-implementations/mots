package org.motechproject.mots.validate;

import java.util.Optional;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class InchargeValidator extends AbstractValidator {

  private static final String ERROR_CODE = "errorCode";

  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String FACILITY_ID = "facilityId";

  @Autowired
  private InchargeRepository inchargeRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return Incharge.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Incharge incharge = (Incharge) target;

    validatePhoneNumberUniqueness(errors, incharge);
    validateFacilityUniqueness(errors, incharge);
    validateIfFacilityAlreadyExists(errors, incharge.getFacility());
  }

  private void validatePhoneNumberUniqueness(Errors errors, Incharge incharge) {
    Optional<Incharge> existingIncharge = inchargeRepository.findByPhoneNumber(incharge.getPhoneNumber());
    existingIncharge.ifPresent(foundedIncharge -> {
      if (!foundedIncharge.getId().equals(incharge.getId())) {
        errors.rejectValue(PHONE_NUMBER, ERROR_CODE, ValidationMessages.NOT_UNIQUE_PHONE_NUMBER);
      }
    });
  }

  private void validateFacilityUniqueness(Errors errors, Incharge incharge) {
    Optional<Incharge> existingIncharge = inchargeRepository.findByFacility(incharge.getFacility());
    existingIncharge.ifPresent(foundedIncharge -> {
      if (!foundedIncharge.getId().equals(incharge.getId())) {
        errors.rejectValue(FACILITY_ID, ERROR_CODE, ValidationMessages.NOT_UNIQUE_FACILITY);
      }
    });
  }

  private void validateIfFacilityAlreadyExists(Errors errors, Facility facility) {
    if (facility != null && facility.getId() != null) {
      if (!facilityRepository.exists(facility.getId())) {
        errors.rejectValue(FACILITY_ID, ERROR_CODE, ValidationMessages.NOT_EXISITNG_FACILITY);
      }
    }
  }
}
