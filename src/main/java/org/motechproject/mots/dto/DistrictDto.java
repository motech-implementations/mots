package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class DistrictDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Set<ChiefdomDto> chiefdoms;
}
