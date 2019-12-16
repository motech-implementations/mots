package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
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

  @Getter
  @Setter
  private String inchargeFullName;

  @Getter
  @Setter
  private String inchargePhone;

  @Getter
  @Setter
  private String inchargeEmail;

  @Getter
  @Setter
  private String ownerUsername;
}
