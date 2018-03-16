package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.UniqueFacilityId;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueFacilityIdValidator
    implements ConstraintValidator<UniqueFacilityId, FacilityCreationDto> {

  private static final String FACILITY_ID = "facilityId";

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(FacilityCreationDto facilityCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(facilityCreationDto.getFacilityId())) {
      Optional<Facility> existing =
          facilityRepository.findByFacilityId(facilityCreationDto.getFacilityId());
      if (existing.isPresent() // when edit facility allows change
          && !existing.get().getId().toString().equals(facilityCreationDto.getId())) {

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context,
            FACILITY_ID, ValidationMessages.NOT_UNIQUE_FACILITY_ID);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(UniqueFacilityId parameters) {
    // we don't need any passed parameters
  }
}


