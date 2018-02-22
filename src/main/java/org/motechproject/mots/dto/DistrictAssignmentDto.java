package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class DistrictAssignmentDto {

  @Getter
  @Setter
  private String districtId;

  @Getter
  @Setter
  private Set<String> modules;

  @Getter
  @Setter
  private String startDate;

  @Getter
  @Setter
  private String endDate;
}
