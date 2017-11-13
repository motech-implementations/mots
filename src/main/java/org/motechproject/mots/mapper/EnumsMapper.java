package org.motechproject.mots.mapper;

import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.Literacy;

public class EnumsMapper {

  /**
   * Get Gender object from display name.
   * @param displayName display name of Gender
   * @return Gender object
   */
  public Gender toGender(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return Gender.getByDisplayName(displayName);
  }

  /**
   * Get display name from Gender.
   * @return display name of Gender
   */
  public String fromGender(Gender gender) {
    if (gender == null) {
      return null;
    }

    return gender.getDisplayName();
  }

  /**
   * Get Literacy object from display name.
   * @param displayName display name of Literacy
   * @return Literacy object
   */
  public Literacy toLiteracy(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return Literacy.getByDisplayName(displayName);
  }

  /**
   * Get display name from Literacy.
   * @return display name of Literacy
   */
  public String fromLiteracy(Literacy literacy) {
    if (literacy == null) {
      return null;
    }

    return literacy.getDisplayName();
  }

  /**
   * Get EducationLevel object from display name.
   * @param displayName display name of EducationLevel
   * @return EducationLevel object
   */
  public EducationLevel toEducationLevel(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return EducationLevel.getByDisplayName(displayName);
  }

  /**
   * Get display name from EducationLevel.
   * @return display name of EducationLevel
   */
  public String fromEducationLevel(EducationLevel educationLevel) {
    if (educationLevel == null) {
      return null;
    }

    return educationLevel.getDisplayName();
  }

  /**
   * Get Language object from display name.
   * @param displayName display name of Language
   * @return Language object
   */
  public Language toLanguage(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return Language.getByDisplayName(displayName);
  }

  /**
   * Get display name from Language.
   * @return display name of Language
   */
  public String fromLanguage(Language language) {
    if (language == null) {
      return null;
    }

    return language.getDisplayName();
  }
}
