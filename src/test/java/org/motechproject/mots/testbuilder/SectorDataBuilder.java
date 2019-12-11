package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.domain.Sector;

public class SectorDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String name;
  private District district;

  /**
   * Returns instance of {@link SectorDataBuilder} with sample data.
   */
  public SectorDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Sector #" + instanceNumber;
    district = new DistrictDataBuilder().build();
  }

  /**
   * Builds instance of {@link Sector} without id.
   */
  public Sector buildAsNew() {

    Sector sector = new Sector();
    sector.setName(name);
    sector.setDistrict(district);

    return sector;
  }

  /**
   * Builds instance of {@link Sector}.
   */
  public Sector build() {
    Sector sector = buildAsNew();
    sector.setId(id);

    return sector;
  }

  /**
   * Adds district for new {@link Sector}.
   */
  public SectorDataBuilder withDistrict(District district) {
    this.district = district;
    return this;
  }
}
