package org.motechproject.mots.testbuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;

public final class UserDataBuilder {

  private static int instanceNumber;

  private final UUID id;
  private final String name;
  private final String username;
  private final String password;
  private final String email;
  private final boolean enabled;
  private UserRole role;

  /**
   * Returns instance of {@link UserDataBuilder} with sample data.
   */
  public UserDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "User #" + instanceNumber;
    username = "Username #" + instanceNumber;
    email = "UserEmail#" + instanceNumber + "@test.com";
    password = "User password #" + instanceNumber;
    enabled = true;
  }

  /**
   * Builds instance of {@link User} without id.
   */
  public User buildAsNew() {

    User user = new User();
    user.setName(name);
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    Set<UserRole> roles = new HashSet<>();
    roles.add(role);
    user.setRoles(roles);
    user.setEnabled(enabled);

    return user;
  }

  /**
   * Builds instance of {@link User}.
   */
  public User build() {
    User user = buildAsNew();
    user.setId(id);

    return user;
  }

  /**
   * Adds Role for new {@link User}.
   */
  public UserDataBuilder withRole(UserRole userRole) {
    this.role = userRole;
    return this;
  }
}
