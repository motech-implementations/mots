package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.dto.SectorCreationDto;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.repository.SectorRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.SectorUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class SectorUniquenessValidator implements
    ConstraintValidator<SectorUniqueness, SectorCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Override
  public boolean isValid(SectorCreationDto sectorCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(sectorCreationDto.getDistrictId())
        && ValidationUtils.isValidUuidString(sectorCreationDto.getDistrictId())
        && StringUtils.isNotEmpty(sectorCreationDto.getName())) {

      String name = sectorCreationDto.getName();
      UUID districtId = UUID.fromString(sectorCreationDto.getDistrictId());
      District district = districtRepository.findOne(districtId);

      Sector existing = sectorRepository.findByNameAndDistrict(name, district);

      if (existing != null // when edit sector allows change
          && !existing.getId().toString().equals(sectorCreationDto.getId())) {
        String message = String.format(ValidationMessageConstants.NOT_UNIQUE_SECTOR,
            existing.getName());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(SectorUniqueness parameters) {
    // we don't need any passed parameters
  }
}
