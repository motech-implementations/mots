package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.domain.Facility;

public class CommunityDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String name;
  private Facility facility;

  /**
   * Returns instance of {@link CommunityDataBuilder} with sample data.
   */
  public CommunityDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Community #" + instanceNumber;
    facility = new FacilityDataBuilder().build();
  }

  /**
   * Builds instance of {@link Community} without id.
   */
  public Community buildAsNew() {

    Community community = new Community();
    community.setName(name);
    community.setFacility(facility);

    return community;
  }

  /**
   * Builds instance of {@link Community}.
   */
  public Community build() {
    Community community = buildAsNew();
    community.setId(id);

    return community;
  }

  /**
   * Adds facility for new {@link Community}.
   */
  public CommunityDataBuilder withFacility(Facility facility) {
    this.facility = facility;
    return this;
  }
}
