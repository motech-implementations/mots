package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.District;

public final class DistrictDataBuilder {
  private static int instanceNumber;

  private final UUID id;
  private final String name;

  /**
   * Returns instance of {@link DistrictDataBuilder} with sample data.
   */
  public DistrictDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "District #" + instanceNumber;
  }

  /**
   * Builds instance of {@link District} without id.
   */
  public District buildAsNew() {

    District district = new District();
    district.setName(name);

    return district;
  }

  /**
   * Builds instance of {@link District}.
   */
  public District build() {
    District district = buildAsNew();
    district.setId(id);

    return district;
  }
}
