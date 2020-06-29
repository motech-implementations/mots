package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum Language {
  KRIO("Krio"),
  MENDE("Mende");

  @Getter
  private String displayName;

  Language(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static Language getByDisplayName(String name) {
    for (Language language : Language.values()) {
      if (language.getDisplayName().equalsIgnoreCase(name)) {
        return language;
      }
    }

    return null;
  }
}
