package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.repository.GroupRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.GroupExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupExistenceValidator
    implements ConstraintValidator<GroupExistence, String> {

  @Autowired
  private GroupRepository groupRepository;

  @Override
  public void initialize(GroupExistence constraintAnnotation) {
  }

  @Override
  public boolean isValid(String groupId, ConstraintValidatorContext context) {
    if (StringUtils.isNotEmpty(groupId) && ValidationUtils.isValidUuidString(groupId)) {
      UUID group = UUID.fromString(groupId);
      return groupRepository.exists(group);
    }
    return true;
  }
}
