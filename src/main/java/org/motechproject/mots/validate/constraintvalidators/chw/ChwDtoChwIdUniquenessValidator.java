package org.motechproject.mots.validate.constraintvalidators.chw;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.dto.CommunityHealthWorkerDto;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.ChwIdUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class ChwDtoChwIdUniquenessValidator implements
    ConstraintValidator<ChwIdUniqueness, CommunityHealthWorkerDto> {

  private static final String CHW_ID = "chwId";

  @Autowired
  private CommunityHealthWorkerRepository chwRepository;

  @Override
  public boolean isValid(CommunityHealthWorkerDto chwDto, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(chwDto.getChwId())) {
      Optional<CommunityHealthWorker> existingChw = chwRepository.findByChwId(chwDto.getChwId());

      if (existingChw.isPresent()
          && !StringUtils.equals(chwDto.getId(), existingChw.get().getId().toString())) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, CHW_ID);
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(ChwIdUniqueness parameters) {
    // we don't need any passed parameters
  }
}
