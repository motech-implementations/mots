package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class InchargeDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  @NotEmpty
  private String firstName;

  @Getter
  @Setter
  @NotEmpty
  private String secondName;

  @Getter
  @Setter
  private String otherName;

  @Getter
  @Setter
  @NotEmpty
  private String phoneNumber;

  @Getter
  @Setter
  @Email
  private String email;

  @Getter
  @Setter
  @NotEmpty
  private String districtId;

  @Getter
  @Setter
  @NotEmpty
  private String chiefdomId;

  @Getter
  @Setter
  @NotEmpty
  private String facilityId;

  @Getter
  @Setter
  private String facilityName;
}
