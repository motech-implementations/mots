package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.utils.TrimTextDeserializer;
import org.motechproject.mots.validate.annotations.ChwIdUniqueness;
import org.motechproject.mots.validate.annotations.DistrictExistence;
import org.motechproject.mots.validate.annotations.FacilityExistence;
import org.motechproject.mots.validate.annotations.Gender;
import org.motechproject.mots.validate.annotations.Language;
import org.motechproject.mots.validate.annotations.PhoneNumber;
import org.motechproject.mots.validate.annotations.PhoneNumberUniqueness;
import org.motechproject.mots.validate.annotations.SectorExistence;
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
  @NotBlank(message = ValidationMessageConstants.EMPTY_CHW_ID)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String chwId;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_FIRST_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_FAMILY_NAME)
  @JsonDeserialize(using = TrimTextDeserializer.class)
  private String familyName;

  @Getter
  @Setter
  @Gender
  private String gender;

  @Getter
  @Setter
  @PhoneNumber
  @NotBlank(message = ValidationMessageConstants.EMPTY_PHONE_NUMBER)
  private String phoneNumber;

  @Getter
  @Setter
  private String districtName;

  @Getter
  @Setter
  @DistrictExistence
  @Uuid(message = ValidationMessageConstants.INVALID_DISTRICT_ID)
  @NotEmpty(message = ValidationMessageConstants.EMPTY_DISTRICT)
  private String districtId;

  @Getter
  @Setter
  private String sectorName;

  @Getter
  @Setter
  @SectorExistence
  @Uuid(message = ValidationMessageConstants.INVALID_SECTOR_ID)
  private String sectorId;

  @Getter
  @Setter
  private String facilityName;

  @Getter
  @Setter
  @FacilityExistence
  @Uuid(message = ValidationMessageConstants.INVALID_FACILITY_ID)
  private String facilityId;

  @Getter
  @Setter
  private String villageName;

  @Getter
  @Setter
  @VillageExistence
  @Uuid(message = ValidationMessageConstants.INVALID_VILLAGE_ID)
  private String villageId;

  @Getter
  @Setter
  @Language
  @NotEmpty(message = ValidationMessageConstants.EMPTY_PREFERRED_LANGUAGE)
  private String preferredLanguage;

  @Getter
  @Setter
  private String groupName;

  @Getter
  @Setter
  @Uuid
  private String groupId;
}
