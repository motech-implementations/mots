package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.ChwIdUniqueness;
import org.motechproject.mots.validate.annotations.CommunityExistence;
import org.motechproject.mots.validate.annotations.DateFormat;
import org.motechproject.mots.validate.annotations.EducationLevel;
import org.motechproject.mots.validate.annotations.Gender;
import org.motechproject.mots.validate.annotations.Language;
import org.motechproject.mots.validate.annotations.Literacy;
import org.motechproject.mots.validate.annotations.PastDate;
import org.motechproject.mots.validate.annotations.PhoneNumber;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;

@PhoneNumberUniqueness
@ChwIdUniqueness
public class CommunityHealthWorkerDto {

  @Getter
  @Setter
  @Uuid(message = ValidationMessages.INVALID_ID)
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_CHW_ID)
  private String chwId;

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
  @DateFormat
  @PastDate
  private String dateOfBirth;

  @Getter
  @Setter
  @Gender
  @NotEmpty(message = ValidationMessages.EMPTY_GENDER)
  private String gender;

  @Getter
  @Setter
  @Literacy
  private String literacy;

  @Getter
  @Setter
  @EducationLevel
  private String educationLevel;

  @Getter
  @Setter
  @PhoneNumber
  @NotBlank(message = ValidationMessages.EMPTY_PHONE_NUMBER)
  private String phoneNumber;

  @Getter
  @Setter
  private String districtName;

  @Getter
  @Setter
  private String districtId;

  @Getter
  @Setter
  private String chiefdomName;

  @Getter
  @Setter
  private String chiefdomId;

  @Getter
  @Setter
  private String facilityName;

  @Getter
  @Setter
  private String facilityId;

  @Getter
  @Setter
  private String communityName;

  @Getter
  @Setter
  @CommunityExistence
  @Uuid(message = ValidationMessages.INVALID_COMMUNITY_ID)
  @NotEmpty(message = ValidationMessages.EMPTY_COMMUNITY)
  private String communityId;

  @Getter
  @Setter
  private Boolean hasPeerSupervisor;

  @Getter
  @Setter
  @Language
  @NotEmpty(message = ValidationMessages.EMPTY_PREFERRED_LANGUAGE)
  private String preferredLanguage;
}
