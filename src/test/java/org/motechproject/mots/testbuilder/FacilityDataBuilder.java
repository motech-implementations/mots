package org.motechproject.mots.testbuilder;

import java.util.Random;
import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.enums.FacilityType;

public class FacilityDataBuilder {
  private static final Random RANDOM = new Random();

  private static int instanceNumber = 0;

  private UUID id;
  private String facilityId;
  private String name;
  private FacilityType facilityType;
  private Chiefdom chiefdom;

  /**
   * Returns instance of {@link ChiefdomDataBuilder} with sample data.
   */
  public FacilityDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    facilityId = id.toString();
    name = "Facility #" + instanceNumber;
    facilityType = FacilityType.values()[RANDOM.nextInt(FacilityType.values().length)];
    chiefdom = new ChiefdomDataBuilder().build();
  }

  /**
   * Builds instance of {@link Facility} without id.
   */
  public Facility buildAsNew() {

    Facility facility = new Facility();
    facility.setFacilityId(facilityId);
    facility.setName(name);
    facility.setType(facilityType);
    facility.setChiefdom(chiefdom);

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
   * Adds Chiefdom for new {@link Facility}.
   */
  public FacilityDataBuilder withChiefdom(Chiefdom chiefdom) {
    this.chiefdom = chiefdom;
    return this;
  }
}
