package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class LocationPreviewDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_LOCATION_NAME)
  private String name;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String parent;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String districtName;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String sectorName;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String facilityType;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String inchargeFullName;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String inchargePhone;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String inchargeEmail;

  @Getter
  @Setter
  private String ownerUsername;
}
