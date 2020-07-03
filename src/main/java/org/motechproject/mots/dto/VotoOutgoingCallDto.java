package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotoOutgoingCallDto {

  @Getter
  @Setter
  private String callId;

  @Getter
  @Setter
  private String treeId;

  @JsonProperty("outgoing_call")
  protected void unpackNestedObject(Map<String, Object> call) {
    callId = call.get("id") == null ? null : call.get("id").toString();
    treeId = call.get("tree_id") == null ? null : call.get("tree_id").toString();
  }
}
