package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;

public final class FacilityDataBuilder {

  private static int instanceNumber;

  private final UUID id;
  private final String name;
  private Sector sector;

  /**
   * Returns instance of {@link SectorDataBuilder} with sample data.
   */
  public FacilityDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Facility #" + instanceNumber;
    sector = new SectorDataBuilder().build();
  }

  /**
   * Builds instance of {@link Facility} without id.
   */
  public Facility buildAsNew() {

    Facility facility = new Facility();
    facility.setName(name);
    facility.setSector(sector);

    return facility;
  }

  /**
   * Builds instance of {@link Facility}.
   */
  public Facility build() {
    Facility facility = buildAsNew();
    facility.setId(id);

    return facility;
  }

  /**
   * Adds Sector for new {@link Facility}.
   */
  public FacilityDataBuilder withSector(Sector sector) {
    this.sector = sector;
    return this;
  }
}
