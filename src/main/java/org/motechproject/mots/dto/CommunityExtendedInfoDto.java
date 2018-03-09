package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.validate.annotations.Uuid;

public class CommunityExtendedInfoDto {

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
  private String chiefdomId;

  @Setter
  @Getter
  @Uuid
  private String facilityId;
}
