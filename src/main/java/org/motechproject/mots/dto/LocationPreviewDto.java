package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class LocationPreviewDto {

  @Getter
  @Setter
  @NotNull
  private String id;

  @Getter
  @Setter
  @NotNull
  private String name;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String parent;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String facilityType;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String facilityId;

  @Setter
  @Getter
  @JsonInclude(Include.NON_NULL)
  private String inchargeFullName;

}
