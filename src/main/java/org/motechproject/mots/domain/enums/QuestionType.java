package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum QuestionType {
  PRE_TEST("Pre-test"),
  POST_TEST("Post-test");

  @Getter
  private String displayName;

  QuestionType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static QuestionType getByDisplayName(String name) {
    for (QuestionType type : QuestionType.values()) {
      if (type.getDisplayName().equalsIgnoreCase(name)) {
        return type;
      }
    }

    return null;
  }
}
