package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.dto.ChiefdomCreationDto;
import org.motechproject.mots.repository.ChiefdomRepository;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.ChiefdomUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class ChiefdomUniquenessValidator implements
    ConstraintValidator<ChiefdomUniqueness, ChiefdomCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private ChiefdomRepository chiefdomRepository;

  @Autowired
  private DistrictRepository districtRepository;

  @Override
  public boolean isValid(ChiefdomCreationDto chiefdomCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(chiefdomCreationDto.getDistrictId())
        && ValidationUtils.isValidUuidString(chiefdomCreationDto.getDistrictId())
        && StringUtils.isNotEmpty(chiefdomCreationDto.getName())) {

      String name = chiefdomCreationDto.getName();
      UUID districtId = UUID.fromString(chiefdomCreationDto.getDistrictId());
      District district = districtRepository.findOne(districtId);

      Optional<Chiefdom> existing = chiefdomRepository.findByNameAndDistrict(name, district);

      if (existing.isPresent() // when edit chiefdom allows change
          && !existing.get().getId().toString().equals(chiefdomCreationDto.getId())) {
        String message = String.format(ValidationMessages.NOT_UNIQUE_CHIEFDOM,
            existing.get().getName());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(ChiefdomUniqueness parameters) {
    // we don't need any passed parameters
  }
}
