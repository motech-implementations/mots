package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.GroupExistence;
import org.motechproject.mots.validate.annotations.Uuid;

public class GroupAssignmentDto extends BulkAssignmentDto {

  @Getter
  @Setter
  @GroupExistence
  @Uuid(message = ValidationMessageConstants.INVALID_GROUP_ID)
  @NotEmpty(message = ValidationMessageConstants.EMPTY_GROUP_ID)
  private String groupId;
}
