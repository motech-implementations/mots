package org.motechproject.mots.validate.constraintvalidators;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.InchargeRepository;
import org.motechproject.mots.validate.annotations.incharge.InchargeDtoFacilityUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class InchargeDtoFacilityUniquenessValidator implements
    ConstraintValidator<InchargeDtoFacilityUniqueness, InchargeDto> {

  private static final String ERROR_CODE = "errorCode";

  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String FACILITY_ID = "facility";

  private Map<String, String> errors = new HashMap<>();

  @Autowired
  private InchargeRepository inchargeRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(InchargeDto inchargeDto, ConstraintValidatorContext context) {
  //  return StringUtils.isEmpty(value) || ValidationUtils.matchPattern(PATTERN, value);
    return false;
  }

  @Override
  public void initialize(InchargeDtoFacilityUniqueness parameters) {
    // we don't need any passed parameters
  }

//  private void validatePhoneNumberUniqueness(Map<String, String> errors, InchargeDto inchargeDto) {
//    Optional<Incharge> existingIncharge = inchargeRepository.findByPhoneNumber(
//        inchargeDto.getPhoneNumber());
//    UUID id = UUID.fromString(inchargeDto.getId());
//
//
//    existingIncharge.ifPresent(foundedIncharge -> {
//      if (!foundedIncharge.getId().equals(id)) {
//        errors.rejectValue(PHONE_NUMBER, ERROR_CODE, ValidationMessages.NOT_UNIQUE_PHONE_NUMBER);
//      }
//    });
//  }
//
//  private void validateFacilityUniqueness(Incharge incharge) {
//    Optional<Incharge> existingIncharge = inchargeRepository.findByFacility(incharge.getFacility());
//    existingIncharge.ifPresent(foundedIncharge -> {
//      if (!foundedIncharge.getId().equals(incharge.getId())) {
//     //   errors.rejectValue(FACILITY_ID, ERROR_CODE, ValidationMessages.NOT_UNIQUE_FACILITY);
//      }
//    });
//  }
//
//  private void validateIfFacilityAlreadyExists(Facility facility) {
//    if (facility != null && facility.getId() != null
//        && !facilityRepository.exists(facility.getId())) {
//     // errors.rejectValue(FACILITY_ID, ERROR_CODE, ValidationMessages.NOT_EXISTING_FACILITY);
//    }
//  }
}
