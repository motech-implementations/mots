package org.motechproject.mots.constants;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DefaultPermissions {

  public static final String CHW_READ = "ROLE_CHW_READ";
  public static final String CHW_WRITE = "ROLE_CHW_WRITE";
  public static final String INCHARGE_READ = "ROLE_INCHARGE_READ";
  public static final String INCHARGE_WRITE = "ROLE_INCHARGE_WRITE";
  public static final String MANAGE_MODULES = "ROLE_MANAGE_MODULES";
  public static final String DISPLAY_MODULES = "ROLE_DISPLAY_MODULES";
  public static final String MANAGE_FACILITIES = "ROLE_MANAGE_FACILITIES";
  public static final String MANAGE_OWN_FACILITIES = "ROLE_MANAGE_OWN_FACILITIES";
  public static final String CREATE_FACILITIES = "ROLE_CREATE_FACILITIES";
  public static final String DISPLAY_FACILITIES = "ROLE_DISPLAY_FACILITIES";
  public static final String ASSIGN_MODULES = "ROLE_ASSIGN_MODULES";
  public static final String MANAGE_USERS = "ROLE_MANAGE_USERS";
  public static final String MANAGE_INCHARGE_USERS = "ROLE_MANAGE_INCHARGE_USERS";
  public static final String DISPLAY_REPORTS = "ROLE_DISPLAY_REPORTS";
  public static final String UPLOAD_CSV = "ROLE_UPLOAD_CSV";

  public static final String HAS_CHW_READ_ROLE = "hasRole('" + CHW_READ + "')";
  public static final String HAS_CHW_WRITE_ROLE = "hasRole('" + CHW_WRITE + "')";
  public static final String HAS_INCHARGE_WRITE_ROLE = "hasRole('" + INCHARGE_WRITE + "')";
  public static final String HAS_INCHARGE_READ_ROLE = "hasRole('" + INCHARGE_READ + "')";
  public static final String HAS_MANAGE_MODULES_ROLE = "hasRole('" + MANAGE_MODULES + "')";
  public static final String HAS_DISPLAY_MODULES_ROLE = "hasRole('" + DISPLAY_MODULES + "')";
  public static final String HAS_MANAGE_FACILITIES_ROLE = "hasRole('" + MANAGE_FACILITIES + "')";
  public static final String HAS_CREATE_FACILITIES_ROLE = "hasRole('" + CREATE_FACILITIES + "')";
  public static final String HAS_DISPLAY_FACILITIES_ROLE =
      "hasRole('" + DISPLAY_FACILITIES + "')";
  public static final String HAS_ASSIGN_MODULES_ROLE = "hasRole('" + ASSIGN_MODULES + "')";
  public static final String HAS_MANAGE_USERS_ROLE = "hasRole('" + MANAGE_USERS + "')";
  public static final String HAS_MANAGE_INCHARGE_USERS_ROLE =
      "hasRole('" + MANAGE_INCHARGE_USERS + "')";
  public static final String HAS_DISPLAY_REPORTS_ROLE = "hasRole('" + DISPLAY_REPORTS + "')";
  public static final String HAS_MANAGE_OWN_FACILITIES =
      "hasRole('" + MANAGE_OWN_FACILITIES + "')";
  public static final String HAS_UPLOAD_CSV_ROLE = "hasRole('" + UPLOAD_CSV + "')";

  public static final String HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE =
      "hasAnyRole('" + ASSIGN_MODULES + "','" + MANAGE_MODULES + "','" + DISPLAY_MODULES + "')";

  public static final String HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE =
      "hasAnyRole('" + MANAGE_FACILITIES + "','" + MANAGE_OWN_FACILITIES + "')";

  public static final String HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE =
      "hasAnyRole('" + MANAGE_USERS + "','" + MANAGE_INCHARGE_USERS + "')";
}
