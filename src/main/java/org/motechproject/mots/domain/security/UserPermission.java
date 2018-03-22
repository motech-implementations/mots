package org.motechproject.mots.domain.security;

import lombok.Getter;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public enum UserPermission {
  CHW_READ(RoleNames.CHW_READ, "Display CHWs"),
  CHW_WRITE(RoleNames.CHW_WRITE, "Create/Edit CHW"),
  INCHARGE_READ(RoleNames.INCHARGE_READ, "Display Incharges"),
  INCHARGE_WRITE(RoleNames.INCHARGE_WRITE, "Create/Edit CHW"),
  ASSIGN_MODULES(RoleNames.ASSIGN_MODULES, "Assign Modules"),
  DISPLAY_MODULES(RoleNames.DISPLAY_MODULES, "Display Modules"),
  MANAGE_MODULES(RoleNames.MANAGE_MODULES, "Manage Modules"),
  MANAGE_FACILITIES(RoleNames.MANAGE_FACILITIES, "Manage Facilities"),
  MANAGE_OWN_FACILITIES(RoleNames.MANAGE_OWN_FACILITIES, "Manage Own Facilities"),
  CREATE_FACILITIES(RoleNames.CREATE_FACILITIES, "Create Facilities"),
  DISPLAY_FACILITIES(RoleNames.DISPLAY_FACILITIES, "Display Facilities"),
  MANAGE_USERS(RoleNames.MANAGE_USERS, "Manage Users"),
  DISPLAY_REPORTS(RoleNames.DISPLAY_REPORTS, "Display Reports");

  @Getter
  private String roleName;

  @Getter
  private String displayName;

  UserPermission(String name, String displayName) {
    this.roleName = name;
    this.displayName = displayName;
  }

  public static class RoleNames {

    private static final String CHW_READ = "ROLE_CHW_READ";
    private static final String CHW_WRITE = "ROLE_CHW_WRITE";
    private static final String INCHARGE_READ = "ROLE_INCHARGE_READ";
    private static final String INCHARGE_WRITE = "ROLE_INCHARGE_WRITE";
    private static final String MANAGE_MODULES = "ROLE_MANAGE_MODULES";
    private static final String DISPLAY_MODULES = "ROLE_DISPLAY_MODULES";
    private static final String MANAGE_FACILITIES = "ROLE_MANAGE_FACILITIES";
    private static final String MANAGE_OWN_FACILITIES = "ROLE_MANAGE_OWN_FACILITIES";
    private static final String CREATE_FACILITIES = "ROLE_CREATE_FACILITIES";
    private static final String DISPLAY_FACILITIES = "ROLE_DISPLAY_FACILITIES";
    private static final String ASSIGN_MODULES = "ROLE_ASSIGN_MODULES";
    private static final String MANAGE_USERS = "ROLE_MANAGE_USERS";
    private static final String DISPLAY_REPORTS = "ROLE_DISPLAY_REPORTS";

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
    public static final String HAS_DISPLAY_REPORTS_ROLE = "hasRole('" + DISPLAY_REPORTS + "')";
    public static final String HAS_MANAGE_OWN_FACILITIES =
        "hasRole('" + MANAGE_OWN_FACILITIES + "')";

    public static final String HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE =
        "hasAnyRole('" + ASSIGN_MODULES + "','" + MANAGE_MODULES + "','" + DISPLAY_MODULES + "')";

    public static final String HAS_MANAGE_FACILITIES_OR_MANAGE_OWN_FACILITIES_ROLE =
        "hasAnyRole('" + MANAGE_FACILITIES + "','" + MANAGE_OWN_FACILITIES + "')";

  }
}
