package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum FacilityType {
  CHC("CHC"),
  CHP("CHP"),
  MCHP("MCHP"),
  CLINIC("Clinic"),
  HOSPITAL("Hospital");

  @Getter
  private String displayName;

  FacilityType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static FacilityType getByDisplayName(String name) {
    for (FacilityType facilityType : FacilityType.values()) {
      if (facilityType.getDisplayName().equalsIgnoreCase(name)) {
        return facilityType;
      }
    }

    return null;
  }
}
