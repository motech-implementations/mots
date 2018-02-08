package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.incharge.InchargeDtoDbConstraints;
import org.motechproject.mots.validate.annotations.PhoneNumber;
import org.motechproject.mots.validate.annotations.Uuid;

@InchargeDtoDbConstraints
public class InchargeDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_INCHARGE_ID)
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FIRST_NAME)
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_SECOND_NAME)
  private String secondName;

  @Getter
  @Setter
  private String otherName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_PHONE_NUMBER)
  @PhoneNumber
  private String phoneNumber;

  @Getter
  @Setter
  @Email(message = ValidationMessages.INVALID_EMAIL)
  private String email;

  @Getter
  @Setter
  private String districtId;

  @Getter
  @Setter
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
