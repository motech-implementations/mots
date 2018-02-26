package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.utils.VotoBlockResponseDtoDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotoBlockDto {

  @JsonProperty("block_id")
  @Getter
  @Setter
  private String blockId;

  @JsonProperty("block_type")
  @Getter
  @Setter
  private String blockType;

  @Getter
  @Setter
  private String title;

  @JsonProperty("number_of_repeats")
  @Getter
  @Setter
  private String numberOfRepeats;

  @JsonProperty("entry_at")
  @Getter
  @Setter
  private String entryAt;

  @JsonProperty("exit_at")
  @Getter
  @Setter
  private String exitAt;

  @JsonDeserialize(using = VotoBlockResponseDtoDeserializer.class)
  @Getter
  @Setter
  private VotoBlockResponseDto response;
}
