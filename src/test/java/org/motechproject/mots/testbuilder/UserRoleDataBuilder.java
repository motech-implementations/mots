package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.security.UserRole;

public class UserRoleDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String name;

  /**
   * Returns instance of {@link UserRoleDataBuilder} with sample data.
   */
  public UserRoleDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "User role #" + instanceNumber;
  }

  /**
   * Builds instance of {@link UserRole} without id.
   */
  public UserRole buildAsNew() {

    UserRole userRole = new UserRole();
    userRole.setName(name);

    return userRole;
  }

  /**
   * Builds instance of {@link UserRole}.
   */
  public UserRole build() {
    UserRole userRole = buildAsNew();
    userRole.setId(id);

    return userRole;
  }
}
