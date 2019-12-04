package org.motechproject.mots.mapper;

import org.mapstruct.Mapper;
import org.motechproject.mots.domain.enums.ChoiceType;
import org.motechproject.mots.domain.enums.FacilityType;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.QuestionType;

@Mapper(componentModel = "spring")
@SuppressWarnings("PMD.TooManyMethods")
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

  /**
   * Get FacilityType object from display name.
   * @param displayName display name of FacilityType
   * @return FacilityType object
   */
  public FacilityType toFacilityType(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return FacilityType.getByDisplayName(displayName);
  }

  /**
   * Get display name from FacilityType.
   * @return display name of FacilityType
   */
  public String fromFacilityType(FacilityType type) {
    if (type == null) {
      return null;
    }

    return type.getDisplayName();
  }

  /**
   * Get QuestionType object from display name.
   * @param displayName display name of QuestionType
   * @return QuestionType object
   */
  public QuestionType toQuestionType(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return QuestionType.getByDisplayName(displayName);
  }

  /**
   * Get display name from QuestionType.
   * @return display name of QuestionType
   */
  public String fromQuestionType(QuestionType questionType) {
    if (questionType == null) {
      return null;
    }

    return questionType.getDisplayName();
  }

  /**
   * Get ChoiceType object from display name.
   * @param displayName display name of ChoiceType
   * @return ChoiceType object
   */
  public ChoiceType toChoiceType(String displayName) {
    if (displayName == null || displayName.isEmpty()) {
      return null;
    }

    return ChoiceType.getByDisplayName(displayName);
  }

  /**
   * Get display name from ChoiceType.
   * @return display name of ChoiceType
   */
  public String fromChoiceType(ChoiceType choiceType) {
    if (choiceType == null) {
      return null;
    }

    return choiceType.getDisplayName();
  }
}
