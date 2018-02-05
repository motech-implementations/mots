package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.security.UserPermission;

public class RoleDto {

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<UserPermission> permissions;
}
