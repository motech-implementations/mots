package org.motechproject.mots.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.DateTimeFormat;
import org.motechproject.mots.validate.annotations.ModulesExistence;
import org.motechproject.mots.validate.annotations.Uuid;
import org.motechproject.mots.validate.annotations.Uuids;

public class ModuleAssignmentDto {

  @Getter
  @Setter
  @Uuid
  private String chwId;

  @Getter
  @Setter
  @ModulesExistence
  @Uuids
  @NotEmpty(message = ValidationMessageConstants.EMPTY_MODULES)
  private Set<String> modules;

  @Getter
  @Setter
  @DateTimeFormat
  private String notificationTime;
}
