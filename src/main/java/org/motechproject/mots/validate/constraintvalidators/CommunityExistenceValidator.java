package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.repository.CommunityRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.CommunityExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class CommunityExistenceValidator implements
    ConstraintValidator<CommunityExistence, String> {

  @Autowired
  private CommunityRepository communityRepository;

  @Override
  public boolean isValid(String communityId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(communityId) && ValidationUtils.isValidUuidString(communityId)) {
      UUID community = UUID.fromString(communityId);
      return communityRepository.exists(community);
    }
    return true;
  }

  @Override
  public void initialize(CommunityExistence parameters) {
    // we don't need any passed parameters
  }
}
