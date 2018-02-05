package org.motechproject.mots.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class UserDto {


  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<RoleDto> roles = new HashSet<>();

  @Getter
  @Setter
  private Boolean enabled;
}
