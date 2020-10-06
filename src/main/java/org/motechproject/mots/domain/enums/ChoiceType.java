package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum ChoiceType {
  CORRECT("Correct"),
  INCORRECT("Incorrect"),
  DONT_KNOW("I don't know"),
  REPEAT("Repeat"),
  SURVEY("Survey");

  @Getter
  private String displayName;

  ChoiceType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   * @return display name of ChoiceType
   */
  public static ChoiceType getByDisplayName(String name) {
    for (ChoiceType choiceType : ChoiceType.values()) {
      if (choiceType.getDisplayName().equalsIgnoreCase(name)) {
        return choiceType;
      }
    }

    return null;
  }
}
