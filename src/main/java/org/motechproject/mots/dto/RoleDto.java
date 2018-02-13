package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

public class RoleDto {

  @NotEmpty
  @Getter
  @Setter
  private String id;

  @NotEmpty
  @Getter
  @Setter
  private String name;
}
