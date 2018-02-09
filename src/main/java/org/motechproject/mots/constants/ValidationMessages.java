package org.motechproject.mots.constants;

public final class ValidationMessages {

  public static final String EMPTY_CHW_ID = "CHW Id cannot be empty";
  public static final String EMPTY_FIRST_NAME = "First name cannot be empty";
  public static final String EMPTY_SECOND_NAME = "Surname cannot be empty";
  public static final String EMPTY_GENDER = "Gender cannot be empty";
  public static final String EMPTY_PHONE_NUMBER = "Phone number cannot be empty";
  public static final String EMPTY_COMMUNITY = "Community cannot be empty";
  public static final String EMPTY_PREFERRED_LANGUAGE = "Preferred Language cannot be empty";
  public static final String EMPTY_DATE_OF_BIRTH = "Date of birth cannot be empty";

  public static final String INVALID_EMAIL = "Invalid e-mail";
  public static final String INVALID_ID = "Invalid ID (UUID)";
  public static final String INVALID_FACILITY_ID = "Invalid facility UUID";
  public static final String INVALID_COMMUNITY_ID = "Invalid community UUID";
  public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
  public static final String INVALID_DATE_OF_BIRTH = "Invalid date of birth";
  public static final String INVALID_GENDER = "Invalid gender";
  public static final String INVALID_EDUCATIONAL_LEVEL = "Invalid education level";
  public static final String INVALID_LITERACY = "Invalid literacy";

  public static final String NOT_UNIQUE_PHONE_NUMBER = "Phone number is not unique";
  public static final String NOT_UNIQUE_FACILITY = "Facility is not unique";
  public static final String NOT_UNIQUE_CHW_ID = "CHW Id is not unique";

  public static final String NOT_EXISTING_FACILITY = "Facility doesn't exist";
  public static final String NOT_EXISTING_COMMUNITY = "Community doesn't exist";

  private ValidationMessages() {
  }
}
