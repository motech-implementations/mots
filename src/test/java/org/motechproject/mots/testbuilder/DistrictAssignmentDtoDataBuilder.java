package org.motechproject.mots.testbuilder;

import java.util.HashSet;
import java.util.Set;
import org.motechproject.mots.dto.DistrictAssignmentDto;

public class DistrictAssignmentDtoDataBuilder {

  private String districtId;
  private Set<String> modules = new HashSet<>();
  private String startDate;
  private String endDate;

  /**
   * Returns instance of {@link DistrictAssignmentDtoDataBuilder} with sample data.
   */
  public DistrictAssignmentDtoDataBuilder() {
    startDate = "2018-01-01";
    endDate = "2018-02-01";
  }

  /**
   * Builds instance of {@link DistrictAssignmentDto} without id.
   */
  public DistrictAssignmentDto build() {

    DistrictAssignmentDto districtAssignmentDto = new DistrictAssignmentDto();
    districtAssignmentDto.setDistrictId(districtId);
    districtAssignmentDto.setModules(modules);
    districtAssignmentDto.setStartDate(startDate);
    districtAssignmentDto.setEndDate(endDate);

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
