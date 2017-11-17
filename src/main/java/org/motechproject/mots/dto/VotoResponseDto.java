package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class VotoResponseDto<T> {

  @Getter
  @Setter
  private String status;

  @Getter
  @Setter
  private String code;

  @Getter
  @Setter
  private T data;

  @Getter
  @Setter
  private String message;

  @JsonProperty("more_info")
  @Getter
  @Setter
  private String moreInfo;
}
