package org.motechproject.mots.utils;

import static org.motechproject.mots.constants.MotsConstants.ADMIN_USER;

import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

  @Autowired
  private UserService userService;

  /**
   * Method returns current user based on Spring context.
   *
   * @return User entity of current user.
   */
  public User getCurrentUser() {
    String username =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return userService.getUserByUserName(username);
  }

  /**
   * Method returns admin user.
   *
   * @return User entity of admin user.
   */
  public User getAdminUser() {
    return userService.getUserByUserName(ADMIN_USER);
  }
}
