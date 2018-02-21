package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.Incharge;

public class InchargeDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String firstName;
  private String secondName;
  private String phoneNumber;
  private Facility facility;

  /**
   * Returns instance of {@link InchargeDataBuilder} with sample data.
   */
  public InchargeDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    firstName = "first " + instanceNumber;
    secondName = "second " + instanceNumber;
    phoneNumber = "+" + instanceNumber;
    facility = new FacilityDataBuilder().buildAsNew();
  }

  /**
   * Builds instance of {@link Incharge} without id.
   */
  public Incharge buildAsNew() {
    Incharge incharge = new Incharge();
    incharge.setFirstName(firstName);
    incharge.setSecondName(secondName);
    incharge.setPhoneNumber(phoneNumber);
    incharge.setFacility(facility);
    return incharge;
  }

  /**
   * Builds instance of {@link Incharge}.
   */
  public Incharge build() {
    Incharge incharge = buildAsNew();
    incharge.setId(id);

    return incharge;
  }

  /**
   * Adds facility for new {@link Incharge}.
   */
  public InchargeDataBuilder withFacility(Facility facility) {
    this.facility = facility;
    return this;
  }
}
