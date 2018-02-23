package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotoBlockResponseDto {

  @JsonProperty("choice_id")
  @Getter
  @Setter
  private String choiceId;

  @JsonProperty("choice_name")
  @Getter
  @Setter
  private String choiceName;
}
