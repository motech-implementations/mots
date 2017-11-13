package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum EducationLevel {
  PRIMARY("Primary"),
  SECONDARY("Secondary"),
  HIGHER("Higher"),
  NONE("None");

  @Getter
  private String displayName;

  EducationLevel(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static EducationLevel getByDisplayName(String name) {
    for (EducationLevel educationLevel : EducationLevel.values()) {
      if (educationLevel.getDisplayName().equalsIgnoreCase(name)) {
        return educationLevel;
      }
    }

    return null;
  }
}
