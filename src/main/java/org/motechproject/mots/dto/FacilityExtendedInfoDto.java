package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.validate.annotations.FacilityType;
import org.motechproject.mots.validate.annotations.Uuid;

public class FacilityExtendedInfoDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  private String name;

  @Setter
  @Getter
  @Uuid
  private String districtId;

  @Setter
  @Getter
  @Uuid
  private String sectorId;

  @Setter
  @Getter
  @FacilityType
  private String facilityType;

  @Setter
  @Getter
  private String facilityId;

  @Getter
  @Setter
  private String ownerUsername;
}
