package org.motechproject.mots.constants;

public final class ValidationMessages {

  public static final String EMPTY_CHW_ID = "CHW Id cannot be empty";
  public static final String EMPTY_IVR_ID = "IVR Id cannot be empty";
  public static final String EMPTY_FIRST_NAME = "First name cannot be empty";
  public static final String EMPTY_SECOND_NAME = "Surname cannot be empty";
  public static final String EMPTY_GENDER = "Gender cannot be empty";
  public static final String EMPTY_PHONE_NUMBER = "Phone number cannot be empty";
  public static final String EMPTY_COMMUNITY = "Community cannot be empty";
  public static final String EMPTY_PREFERRED_LANGUAGE = "Preferred Language cannot be empty";
  public static final String EMPTY_QUESTION_OR_MESSAGE = "Message/Question name cannot be empty";
  public static final String EMPTY_LOCATION_NAME = "Location name cannot be empty";
  public static final String EMPTY_MODULE_NAME = "Module Name cannot be empty";
  public static final String EMPTY_ROLE_NAME = "Role Name cannot be empty";
  public static final String EMPTY_UNIT_NAME = "Unit Name cannot be empty";
  public static final String EMPTY_USERNAME = "Username cannot be empty";
  public static final String EMPTY_CALL_FLOW_ELEMENT_TYPE
      = "Call flow element type cannot be empty";
  public static final String EMPTY_FACILITY_ID = "Facility Id cannot be empty";
  public static final String EMPTY_FACILITY_TYPE = "Facility type cannot be empty";
  public static final String EMPTY_MODULES = "Modules cannot be empty";
  public static final String EMPTY_START_DATE = "Start date cannot be empty";
  public static final String EMPTY_END_DATE = "End date cannot be empty";
  public static final String EMPTY_DISTRICT_ID = "District Id cannot be empty";
  public static final String EMPTY = "Cannot be empty";

  public static final String INVALID_EMAIL = "Invalid e-mail address";
  public static final String INVALID_ID = "Invalid ID (UUID)";
  public static final String INVALID_FACILITY_ID = "Invalid facility UUID";
  public static final String INVALID_DISTRICT_ID = "Invalid district UUID";
  public static final String INVALID_COMMUNITY_ID = "Invalid community UUID";
  public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
  public static final String INVALID_DATE = "Invalid format of date";
  public static final String INVALID_PAST_DATE = "Date must be in the past";
  public static final String INVALID_GENDER = "Invalid gender";
  public static final String INVALID_EDUCATIONAL_LEVEL = "Invalid education level";
  public static final String INVALID_LITERACY = "Invalid literacy";
  public static final String INVALID_CALL_FLOW_ELEMENT_TYPE = "Invalid call flow element type";
  public static final String INVALID_FACILITY_TYPE = "Invalid facility type";
  public static final String INVALID_IDS = "Invalid: %s UUID";

  public static final String NOT_UNIQUE_PHONE_NUMBER = "Phone number is not unique";
  public static final String NOT_UNIQUE_FACILITY = "Facility is not unique";
  public static final String NOT_UNIQUE_CHW_ID
      = "The Community Health Worker with id %s already exists";

  public static final String NOT_EXISTING_FACILITY = "Facility doesn't exist";
  public static final String NOT_EXISTING_COMMUNITY = "Community doesn't exist";
  public static final String NOT_EXISTING_DISTRICT = "District doesn't exist";
  public static final String NOT_EXISTING_CHIEFDOM = "Chiefdom doesn't exist";
  public static final String NOT_EXISTING_MODULES_WITH_ID = "Module with id: %s doesn't exist";

  public static final String END_DATE_AFTER_START_DATE = "End date must be after start date.";

  public static final String NOT_UNIQUE_USERNAME = "The User with username '%s' already exists";

  public static final String PASSWORDS_NOT_MATCH = "New Passwords don't match.";

  private ValidationMessages() {
  }
}
