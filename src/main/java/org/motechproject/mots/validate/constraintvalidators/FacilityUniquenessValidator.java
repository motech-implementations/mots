package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.dto.FacilityCreationDto;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.repository.SectorRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.FacilityUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class FacilityUniquenessValidator implements
    ConstraintValidator<FacilityUniqueness, FacilityCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Override
  public boolean isValid(FacilityCreationDto facilityCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(facilityCreationDto.getSectorId())
        && ValidationUtils.isValidUuidString(facilityCreationDto.getSectorId())
        && StringUtils.isNotEmpty(facilityCreationDto.getName())) {

      String name = facilityCreationDto.getName();
      UUID sectorId = UUID.fromString(facilityCreationDto.getSectorId());
      Sector sector = sectorRepository.findOne(sectorId);

      Optional<Facility> existing = facilityRepository.findByNameAndSector(name, sector);

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
