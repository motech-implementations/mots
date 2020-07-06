package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.dto.VillageCreationDto;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.VillageRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.VillageUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class VillageUniquenessValidator implements
    ConstraintValidator<VillageUniqueness, VillageCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private VillageRepository villageRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(VillageCreationDto villageCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(villageCreationDto.getFacilityId())
        && ValidationUtils.isValidUuidString(villageCreationDto.getFacilityId())
        && StringUtils.isNotEmpty(villageCreationDto.getName())) {

      String name = villageCreationDto.getName();
      UUID facilityId = UUID.fromString(villageCreationDto.getFacilityId());
      Facility facility = facilityRepository.getOne(facilityId);

      Village existing = villageRepository.findByNameAndFacility(name, facility);

      if (existing != null // when edit village allows change
          && !existing.getId().toString().equals(villageCreationDto.getId())) {
        String message = String.format(ValidationMessageConstants.NOT_UNIQUE_VILLAGE,
            existing.getName());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(VillageUniqueness parameters) {
    // we don't need any passed parameters
  }
}
