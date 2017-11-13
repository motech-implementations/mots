package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum Gender {
  MALE("Male"),
  FEMALE("Female");

  @Getter
  private String displayName;

  Gender(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static Gender getByDisplayName(String name) {
    for (Gender gender : Gender.values()) {
      if (gender.getDisplayName().equalsIgnoreCase(name)) {
        return gender;
      }
    }

    return null;
  }
}
