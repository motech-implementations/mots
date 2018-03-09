package org.motechproject.mots.validate.constraintvalidators;

import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.dto.CommunityCreationDto;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.repository.FacilityRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.CommunityUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class CommunityUniquenessValidator implements
    ConstraintValidator<CommunityUniqueness, CommunityCreationDto> {

  private static final String NAME = "name";

  @Autowired
  private CommunityRepository communityRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  @Override
  public boolean isValid(CommunityCreationDto communityCreationDto,
      ConstraintValidatorContext context) {

    if (StringUtils.isNotEmpty(communityCreationDto.getFacilityId())
        && ValidationUtils.isValidUuidString(communityCreationDto.getFacilityId())
        && StringUtils.isNotEmpty(communityCreationDto.getName())) {

      String name = communityCreationDto.getName();
      UUID facilityId = UUID.fromString(communityCreationDto.getFacilityId());
      Facility facility = facilityRepository.findOne(facilityId);

      Optional<Community> existing = communityRepository.findByNameAndFacility(name, facility);

      if (existing.isPresent()) {
        String message = String.format(ValidationMessages.NOT_UNIQUE_COMMUNITY,
            existing.get().getName(), existing.get().getFacility().getId());

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME, message);
        return false;
      }
    }

    return true;
  }

  @Override
  public void initialize(CommunityUniqueness parameters) {
    // we don't need any passed parameters
  }
}
