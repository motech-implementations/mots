package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.RoleRepository;
import org.motechproject.mots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Finds users matching all of the provided parameters.
   * If there are no parameters, return all users.
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Page<User> searchUsers(String username, String email, String name, String role,
      Pageable pageable) throws IllegalArgumentException {
    return userRepository.search(username, email, name, role, pageable);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Iterable<UserRole> getRoles() {
    return roleRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User getUser(UUID id) {
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("User with id: {0} not found", id.toString()));
  }

  /**
   * Get User by username.
   * @param username of user.
   */
  public User getUserByUsername(String username) {
    return getUserByUserName(username);
  }

  /**
   * Save User with new encoded password (if it's not blank).
   * @param user User to be created.
   * @return saved User
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User saveUser(User user, boolean encodeNewPassword) {
    if (encodeNewPassword) {
      String newPasswordEncoded = new BCryptPasswordEncoder().encode(user.getPassword());
      user.setPassword(newPasswordEncoded);
    }

    return userRepository.save(user);
  }

  /**
   * Updates user's password.
   * @param username of user which password is about to change.
   * @param newPassword is new password value for user.
   * @param oldPassword is current user's password.
   */
  public void changeUserPassword(String username, String newPassword, String oldPassword) {
    User user = getUserByUserName(username);

    if (!passwordsMatch(oldPassword, user.getPassword())) {
      throw new IllegalArgumentException("Old password is incorrect.");
    }

    String newPasswordEncoded = new BCryptPasswordEncoder().encode(newPassword);
    user.setPassword(newPasswordEncoded);
    userRepository.save(user);
  }

  /**
   * Create User with new encoded password.
   * @param user User to be created.
   * @return created User
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User registerNewUser(User user) {

    String newPasswordEncoded = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(newPasswordEncoded);

    return userRepository.save(user);
  }

  public User getUserByUserName(String userName) {
    return userRepository.findOneByUsername(userName).orElseThrow(() ->
        new EntityNotFoundException("User with username: {0} not found", userName));
  }

  private boolean passwordsMatch(String oldPassword, String currentPasswordEncoded) {
    return new BCryptPasswordEncoder().matches(oldPassword, currentPasswordEncoded);
  }

}
