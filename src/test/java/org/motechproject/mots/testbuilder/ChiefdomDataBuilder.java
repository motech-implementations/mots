package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.domain.District;

public class ChiefdomDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String name;
  private District district;

  /**
   * Returns instance of {@link ChiefdomDataBuilder} with sample data.
   */
  public ChiefdomDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Chiefdom #" + instanceNumber;
    district = new DistrictDataBuilder().build();
  }

  /**
   * Builds instance of {@link Chiefdom} without id.
   */
  public Chiefdom buildAsNew() {

    Chiefdom chiefdom = new Chiefdom();
    chiefdom.setName(name);
    chiefdom.setDistrict(district);

    return chiefdom;
  }

  /**
   * Builds instance of {@link Chiefdom}.
   */
  public Chiefdom build() {
    Chiefdom chiefdom = buildAsNew();
    chiefdom.setId(id);

    return chiefdom;
  }

  /**
   * Adds district for new {@link Chiefdom}.
   */
  public ChiefdomDataBuilder withDistrict(District district) {
    this.district = district;
    return this;
  }
}
