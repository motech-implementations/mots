package org.motechproject.mots.constants;

public final class ValidationMessages {

  public static final String EMPTY_CHW_ID = "CHW Id cannot be empty";
  public static final String EMPTY_IVR_ID = "IVR Id cannot be empty";
  public static final String EMPTY_CONTINUATION_QUESTION_IVR_ID =
      "Unit Continuation Question IVR Id cannot be empty";
  public static final String EMPTY_START_MODULE_QUESTION_IVR_ID =
      "Start Module Question IVR Id cannot be empty";
  public static final String EMPTY_NO_MODULES_MESSAGE_IVR_ID =
      "No Modules Message IVR Id cannot be empty";
  public static final String EMPTY_MENU_INTRO_MESSAGE_IVR_ID =
      "Menu Intro Message IVR Id cannot be empty";
  public static final String EMPTY_CHOOSE_MODULE_QUESTION_IVR_ID =
      "Choose Module Question IVR Id cannot be empty";
  public static final String EMPTY_FIRST_NAME = "First name cannot be empty";
  public static final String EMPTY_FAMILY_NAME = "Family name cannot be empty";
  public static final String EMPTY_PHONE_NUMBER = "Phone number cannot be empty";
  public static final String EMPTY_DISTRICT = "District cannot be empty";
  public static final String EMPTY_PREFERRED_LANGUAGE = "Preferred Language cannot be empty";
  public static final String EMPTY_QUESTION_OR_MESSAGE = "Message/Question name cannot be empty";
  public static final String EMPTY_LOCATION_NAME = "Location name cannot be empty";
  public static final String EMPTY_MODULE_NAME = "Module Name cannot be empty";
  public static final String EMPTY_ROLE_NAME = "Role Name cannot be empty";
  public static final String EMPTY_PERMISSION_NAME = "Permission Name cannot be empty";
  public static final String EMPTY_UNIT_NAME = "Unit Name cannot be empty";
  public static final String EMPTY_USERNAME = "Username cannot be empty";
  public static final String EMPTY_CALL_FLOW_ELEMENT_TYPE
      = "Call flow element type cannot be empty";
  public static final String EMPTY_FACILITY_ID = "Facility Id cannot be empty";
  public static final String EMPTY_SECTOR_ID = "Sector Id cannot be empty";
  public static final String EMPTY_MODULES = "Modules cannot be empty";
  public static final String EMPTY_DISTRICT_ID = "District Id cannot be empty";
  public static final String EMPTY_GROUP_ID = "Group Id cannot be empty";

  public static final String INVALID_EMAIL = "Invalid e-mail address";
  public static final String INVALID_ID = "Invalid ID (UUID)";
  public static final String INVALID_FACILITY_ID = "Invalid facility UUID";
  public static final String INVALID_DISTRICT_ID = "Invalid district UUID";
  public static final String INVALID_SECTOR_ID = "Invalid sector UUID";
  public static final String INVALID_GROUP_ID = "Invalid group UUID";
  public static final String INVALID_VILLAGE_ID = "Invalid village UUID";
  public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
  public static final String INVALID_DATE = "Invalid format of date";
  public static final String INVALID_PAST_DATE = "Date must be in the past";
  public static final String INVALID_GENDER = "Invalid gender";
  public static final String INVALID_LANGUAGE = "Invalid language";
  public static final String INVALID_CALL_FLOW_ELEMENT_TYPE = "Invalid call flow element type";
  public static final String INVALID_IDS = "Invalid: %s UUID";

  public static final String NOT_UNIQUE_PHONE_NUMBER = "Phone number is not unique";
  public static final String NOT_UNIQUE_CHW_ID
      = "A Community Health Worker with id %s already exists";

  public static final String NOT_EXISTING_FACILITY = "Facility doesn't exist";
  public static final String NOT_EXISTING_VILLAGE = "Village doesn't exist";
  public static final String NOT_EXISTING_DISTRICT = "District doesn't exist";
  public static final String NOT_EXISTING_SECTOR = "Sector doesn't exist";
  public static final String NOT_EXISTING_MODULES_WITH_ID = "Module with id: %s doesn't exist";
  public static final String NOT_EXISTING_GROUP = "Group doesn't exist";

  public static final String NOT_UNIQUE_USERNAME = "A User with username '%s' already exists";
  public static final String USER_DUPLICATE_EMAIL = "A User with email '%s' already exists";
  public static final String NOT_UNIQUE_FACILITY =
      "A Facility with name: '%s' within sector already exists";
  public static final String NOT_UNIQUE_VILLAGE =
      "A Village with name: '%s' within facility already exists";
  public static final String NOT_UNIQUE_SECTOR =
       "A Sector with name: '%s' within district already exists";
  public static final String NOT_UNIQUE_DISTRICT =
       "A District with name: '%s' already exists";

  public static final String PASSWORDS_NOT_MATCH = "New Passwords don't match.";
  public static final String ONE_CHOICE_CORRECT = "One choice must be correct.";

  public static final String EMPTY_GROUP_NAME = "Group name cannot be empty";
  public static final String NOT_UNIQUE_GROUP_NAME = "A Group with name '%s' already exists";

  private ValidationMessages() {
  }
}
