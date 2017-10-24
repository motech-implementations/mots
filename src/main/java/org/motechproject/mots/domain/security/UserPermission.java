package org.motechproject.mots.domain.security;

import lombok.Getter;

public enum UserPermission {
  USER(RoleNames.USER, "User"),
  ADMIN(RoleNames.ADMIN, "Admin");

  @Getter
  private String roleName;

  @Getter
  private String displayName;

  UserPermission(String name, String displayName) {
    this.roleName = name;
    this.displayName = displayName;
  }

  public static class RoleNames {

    public final static String USER = "ROLE_USER";
    public final static String ADMIN = "ROLE_ADMIN";

    public final static String HAS_USER_ROLE = "hasRole('" + USER + "')";
    public final static String HAS_ADMIN_ROLE = "hasRole('" + ADMIN + "')";
  }
}
