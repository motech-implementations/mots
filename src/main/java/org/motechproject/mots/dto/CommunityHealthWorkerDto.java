package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;

public class CommunityHealthWorkerDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String chwId;

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
  private String dateOfBirth;

  @Getter
  @Setter
  private String gender;

  @Getter
  @Setter
  private String literacy;

  @Getter
  @Setter
  private String educationLevel;

  @Getter
  @Setter
  private String phoneNumber;

  @Getter
  @Setter
  private String districtName;

  @Getter
  @Setter
  private String chiefdomName;

  @Getter
  @Setter
  private String facilityName;

  @Getter
  @Setter
  private String communityName;

  @Getter
  @Setter
  private String communityId;

  @Getter
  @Setter
  private Boolean hasPeerSupervisor;

  @Getter
  @Setter
  private String supervisor;

  @Getter
  @Setter
  private String preferredLanguage;
}
