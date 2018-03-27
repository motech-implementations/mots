package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum Literacy {
  CAN_READ_AND_WRITE("Can read and write"),
  CANNOT_READ_AND_WRITE("Cannot read and write"),
  CAN_ONLY_READ("Can only read");

  @Getter
  private String displayName;

  Literacy(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static Literacy getByDisplayName(String name) {
    for (Literacy literacy : Literacy.values()) {
      if (literacy.getDisplayName().equalsIgnoreCase(name)) {
        return literacy;
      }
    }

    return null;
  }
}
