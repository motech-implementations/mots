package org.motechproject.mots.validate.constraintvalidators;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.dto.GroupDto;
import org.motechproject.mots.repository.GroupRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.GroupNameUniqueness;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupNameUniqunessValidator implements
    ConstraintValidator<GroupNameUniqueness, GroupDto> {

  private static final String NAME = "name";

  @Autowired
  private GroupRepository groupRepository;

  @Override
  public boolean isValid(GroupDto groupDto, ConstraintValidatorContext context) {
    String newGroupName = groupDto.getName();
    if (StringUtils.isNotBlank(newGroupName)) {
      Group existingGroup = groupRepository.findByName(newGroupName);

      if (existingGroup != null && (groupDto.getId() == null
          || !existingGroup.getId().equals(UUID.fromString(groupDto.getId())))) {

        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, NAME,
            String.format(ValidationMessages.NOT_UNIQUE_GROUP_NAME, newGroupName));
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(GroupNameUniqueness parameters) {
    // we don't need any passed parameters
  }
}
