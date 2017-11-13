package org.motechproject.mots.domain.enums;

import lombok.Getter;

public enum ProgressStatus {
  NOT_STARTED("Not started"),
  IN_PROGRESS("In progress"),
  COMPLETED("Completed");

  @Getter
  private String displayName;

  ProgressStatus(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Return an enum value from display name.
   * @param name display name of enum value
   */
  public static ProgressStatus getByDisplayName(String name) {
    for (ProgressStatus progressStatus : ProgressStatus.values()) {
      if (progressStatus.getDisplayName().equalsIgnoreCase(name)) {
        return progressStatus;
      }
    }

    return null;
  }
}
