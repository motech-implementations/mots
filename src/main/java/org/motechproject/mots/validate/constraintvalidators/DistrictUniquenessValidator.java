package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.dto.DistrictCreationDto;
import org.motechproject.mots.repository.DistrictRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.DistrictUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class DistrictUniquenessValidator implements
    ConstraintValidator<DistrictUniqueness, DistrictCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private DistrictRepository districtRepository;

  @Override
  public boolean isValid(DistrictCreationDto districtCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(districtCreationDto.getName())) {

      String name = districtCreationDto.getName();

      Optional<District> existing = districtRepository.findByName(name);

      if (existing.isPresent() // when edit district allows change
          && !existing.get().getId().toString().equals(districtCreationDto.getId())) {
        String message = String.format(ValidationMessages.NOT_UNIQUE_DISTRICT,
            existing.get().getName());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(DistrictUniqueness parameters) {
    // we don't need any passed parameters
  }
}
