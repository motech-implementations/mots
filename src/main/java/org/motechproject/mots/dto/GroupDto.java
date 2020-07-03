package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.GroupNameUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@GroupNameUniqueness
public class GroupDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_GROUP_NAME)
  private String name;
}
