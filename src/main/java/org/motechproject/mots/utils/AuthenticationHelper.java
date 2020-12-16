package org.motechproject.mots.utils;

import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

  private static final String ADMIN_USER = "admin";
  private static final String ADMIN_ROLE = "Admin";

  @Autowired
  private UserRepository userRepository;

  /**
   * Method returns current user based on Spring context.
   *
   * @return User entity of current user.
   */
  public User getCurrentUser() {
    String username =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return getUserByUserName(username);
  }

  /**
   * Method returns admin user.
   *
   * @return User entity of admin user.
   */
  public User getAdminUser() {
    return getUserByUserName(ADMIN_USER);
  }

  /**
   * Method returns true if current user is an admin user.
   *
   * @return User entity of current user.
   */
  public boolean isAdminUser() {
    User user = getCurrentUser();
    return user.getRoles().stream().anyMatch(userRole -> ADMIN_ROLE.equals(userRole.getName()));
  }

  private User getUserByUserName(String username) {
    return userRepository.findOneByUsername(username).orElseThrow(() ->
        new EntityNotFoundException("User with username: {0} not found", username));
  }
}
