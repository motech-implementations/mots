package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;

public class InchargeDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String secondName;

  @Getter
  @Setter
  private String otherName;

  @Getter
  @Setter
  private String phoneNumber;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String facilityId;
}
