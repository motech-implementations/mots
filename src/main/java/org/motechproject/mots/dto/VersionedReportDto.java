package org.motechproject.mots.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class VersionedReportDto {

  @Getter
  @Setter
  private long version;

  @Getter
  @Setter
  private JsonNode data;

  /**
   * Create an instance of VersionedReportDto with a json string.
   * 
   * @param data the generated report (json string)
   * @param version report's version
   * @throws IOException if object mapper fails
   */
  public VersionedReportDto(String data, long version) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    this.version = version;
    this.data = mapper.readTree(data);
  }
}
