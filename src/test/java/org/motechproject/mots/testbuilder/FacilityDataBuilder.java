package org.motechproject.mots.testbuilder;

import java.util.Random;
import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.enums.FacilityType;

public final class FacilityDataBuilder {

  private static final Random RANDOM = new Random();

  private static int instanceNumber;

  private final UUID id;
  private final String name;
  private final FacilityType facilityType;
  private Sector sector;

  /**
   * Returns instance of {@link SectorDataBuilder} with sample data.
   */
  public FacilityDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Facility #" + instanceNumber;
    facilityType = FacilityType.values()[RANDOM.nextInt(FacilityType.values().length)];
    sector = new SectorDataBuilder().build();
  }

  /**
   * Builds instance of {@link Facility} without id.
   */
  public Facility buildAsNew() {

    Facility facility = new Facility();
    facility.setName(name);
    facility.setType(facilityType);
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
