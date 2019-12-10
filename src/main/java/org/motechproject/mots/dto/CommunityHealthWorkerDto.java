package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.ChwIdUniqueness;
import org.motechproject.mots.validate.annotations.Gender;
import org.motechproject.mots.validate.annotations.Language;
import org.motechproject.mots.validate.annotations.PhoneNumber;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.motechproject.mots.validate.annotations.Uuid;
import org.motechproject.mots.validate.annotations.VillageExistence;

@PhoneNumberUniqueness
@ChwIdUniqueness
public class CommunityHealthWorkerDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_CHW_ID)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String chwId;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FIRST_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FAMILY_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String familyName;

  @Getter
  @Setter
  @Gender
  private String gender;

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
  private String sectorName;

  @Getter
  @Setter
  private String sectorId;

  @Getter
  @Setter
  private String facilityName;

  @Getter
  @Setter
  private String facilityId;

  @Getter
  @Setter
  private String villageName;

  @Getter
  @Setter
  @VillageExistence
  @Uuid(message = ValidationMessages.INVALID_VILLAGE_ID)
  @NotEmpty(message = ValidationMessages.EMPTY_VILLAGE)
  private String villageId;

  @Getter
  @Setter
  @Language
  @NotEmpty(message = ValidationMessages.EMPTY_PREFERRED_LANGUAGE)
  private String preferredLanguage;

  @Getter
  @Setter
  private String groupName;

  @Getter
  @Setter
  @Uuid
  private String groupId;
}
