package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.utils.TrimTextDeserializer;

public class RegistrationTokenDto {

  @Getter
  @Setter
  private String token;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String email;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<RoleDto> roles = new HashSet<>();
}
