package org.motechproject.mots.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class FacilityDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String type;

  @Getter
  @Setter
  private Map<String, CommunityDto> communities;
}
