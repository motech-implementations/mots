package org.motechproject.mots.testbuilder;

import java.util.Random;
import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.domain.enums.FacilityType;

public class FacilityDataBuilder {
  private static final Random RANDOM = new Random();

  private static int instanceNumber = 0;

  private UUID id;
  private String facilityId;
  private String name;
  private FacilityType facilityType;
  private Sector sector;

  /**
   * Returns instance of {@link SectorDataBuilder} with sample data.
   */
  public FacilityDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    facilityId = id.toString();
    name = "Facility #" + instanceNumber;
    facilityType = FacilityType.values()[RANDOM.nextInt(FacilityType.values().length)];
    sector = new SectorDataBuilder().build();
  }

  /**
   * Builds instance of {@link Facility} without id.
   */
  public Facility buildAsNew() {

    Facility facility = new Facility();
    facility.setFacilityId(facilityId);
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
