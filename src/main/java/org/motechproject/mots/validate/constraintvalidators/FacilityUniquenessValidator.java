package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.FacilityUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class FacilityUniquenessValidator implements
    ConstraintValidator<FacilityUniqueness, FacilityCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Override
  public boolean isValid(FacilityCreationDto facilityCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(facilityCreationDto.getChiefdomId())
        && ValidationUtils.isValidUuidString(facilityCreationDto.getChiefdomId())
        && StringUtils.isNotEmpty(facilityCreationDto.getName())) {

      String name = facilityCreationDto.getName();
      UUID chiefdomId = UUID.fromString(facilityCreationDto.getChiefdomId());
      Chiefdom chiefdom = chiefdomRepository.findOne(chiefdomId);

      Optional<Facility> existing = facilityRepository.findByNameAndChiefdom(name, chiefdom);

      if (existing.isPresent() // when edit facility allows change
          && !existing.get().getId().toString().equals(facilityCreationDto.getId())) {
        String message = String.format(ValidationMessages.NOT_UNIQUE_FACILITY,
                existing.get().getName());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(FacilityUniqueness parameters) {
    // we don't need any passed parameters
  }
}
