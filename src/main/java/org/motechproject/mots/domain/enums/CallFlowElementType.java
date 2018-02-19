package org.motechproject.mots.domain.enums;

public enum CallFlowElementType {
  MESSAGE,
  QUESTION;

  /**
   * Return an enum value from name.
   * @param name name of enum value
   */
  public static CallFlowElementType getByDisplayName(String name) {
    try {
      return CallFlowElementType.valueOf(name);
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }
}
