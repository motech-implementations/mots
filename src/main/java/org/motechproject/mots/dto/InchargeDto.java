package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class InchargeDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessages.EMPTY_FIST_NAME)
  private String firstName;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessages.EMPTY_SECOND_NAME)
  private String secondName;

  @Getter
  @Setter
  private String otherName;

  @Getter
  @Setter
  @NotEmpty(message = ValidationMessages.EMPTY_PHONE_NUMBER)
  private String phoneNumber;

  @Getter
  @Setter
  @Email(message = ValidationMessages.INVALID_EMAIL)
  private String email;

  @Getter
  @Setter
  @NotEmpty
  @Uuid(message = ValidationMessages.INVALID_DISTRICT_ID)
  private String districtId;

  @Getter
  @Setter
  @NotEmpty
  @Uuid(message = ValidationMessages.INVALID_CHIEFDOM_ID)
  private String chiefdomId;

  @Getter
  @Setter
  @NotEmpty
  @Uuid(message = ValidationMessages.INVALID_FACILITY_ID)
  private String facilityId;

  @Getter
  @Setter
  private String facilityName;
}
