package org.motechproject.mots.testbuilder;

import java.util.HashSet;
import java.util.Set;
import org.motechproject.mots.dto.DistrictAssignmentDto;

public class DistrictAssignmentDtoDataBuilder {

  private String districtId;
  private final Set<String> modules = new HashSet<>();

  /**
   * Builds instance of {@link DistrictAssignmentDto} without id.
   */
  public DistrictAssignmentDto build() {

    DistrictAssignmentDto districtAssignmentDto = new DistrictAssignmentDto();
    districtAssignmentDto.setDistrictId(districtId);
    districtAssignmentDto.setModules(modules);

    return districtAssignmentDto;
  }

  /**
   * Adds Module for new {@link DistrictAssignmentDto}.
   */
  public DistrictAssignmentDtoDataBuilder withModule(String moduleId) {
    this.modules.add(moduleId);
    return this;
  }

  /**
   * Adds CommunityHealthWorker for new {@link DistrictAssignmentDto}.
   */
  public DistrictAssignmentDtoDataBuilder withDistrictId(String districtId) {
    this.districtId = districtId;
    return this;
  }
}
