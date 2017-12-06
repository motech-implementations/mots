package org.motechproject.mots.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class ChiefdomDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Map<String, FacilityDto> facilities;
}
