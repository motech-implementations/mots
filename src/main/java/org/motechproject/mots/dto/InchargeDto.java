package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.AssignFacilityToInchargeUniqueness;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.PhoneNumber;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@AssignFacilityToInchargeUniqueness
@PhoneNumberUniqueness
public class InchargeDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FIRST_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_SECOND_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String secondName;

  @Getter
  @Setter
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String otherName;

  @Getter
  @Setter
  @PhoneNumber
  @NotBlank(message = ValidationMessages.EMPTY_PHONE_NUMBER)
  private String phoneNumber;

  @Getter
  @Setter
  @Email(message = ValidationMessages.INVALID_EMAIL)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String email;

  @Getter
  @Setter
  private String districtId;

  @Getter
  @Setter
  private String chiefdomId;

  @Getter
  @Setter
  @FacilityExistence
  @Uuid(message = ValidationMessages.INVALID_FACILITY_ID)
  @NotEmpty
  private String facilityId;

  @Getter
  @Setter
  private String facilityName;
}
