package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class ChwDetailsDto {

  @Getter
  @Setter
  private String language;

  @JsonProperty("subscriber")
  protected void unpackNestedObject(Map<String, Object> subscriber) {
    language = subscriber.get("language_id")
        == null ? null : subscriber.get("language_id").toString();
  }

  @Override
  public String toString() {
    return "ChwDetailsDto{"
        + "language='" + language + '\''
        + '}';
  }
}
