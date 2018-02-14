package org.motechproject.mots.dto;

import java.util.List;
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
  private List<FacilityDto> facilities;
}
