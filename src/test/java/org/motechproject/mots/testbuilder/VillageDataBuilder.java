package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Village;

public final class VillageDataBuilder {

  private static int instanceNumber;

  private final UUID id;
  private final String name;
  private Facility facility;

  /**
   * Returns instance of {@link VillageDataBuilder} with sample data.
   */
  public VillageDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Village #" + instanceNumber;
    facility = new FacilityDataBuilder().build();
  }

  /**
   * Builds instance of {@link Village} without id.
   */
  public Village buildAsNew() {

    Village village = new Village();
    village.setName(name);
    village.setFacility(facility);

    return village;
  }

  /**
   * Builds instance of {@link Village}.
   */
  public Village build() {
    Village village = buildAsNew();
    village.setId(id);

    return village;
  }

  /**
   * Adds facility for new {@link Village}.
   */
  public VillageDataBuilder withFacility(Facility facility) {
    this.facility = facility;
    return this;
  }
}
