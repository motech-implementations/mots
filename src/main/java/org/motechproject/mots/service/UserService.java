package org.motechproject.mots.service;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.RoleRepository;
import org.motechproject.mots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Iterable<UserRole> getRoles() {
    return roleRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User getUser(UUID id) {
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("User with id: {0} not found", id.toString()));
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User saveUser(User user) {
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
   * Create User with randomized password.
   * @param user User to be created.
   * @return created User
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User registerNewUser(User user) {

    String randomGeneratedPassword = RandomStringUtils.random(8, true, false);

    user.setPassword(new BCryptPasswordEncoder().encode(randomGeneratedPassword));

    return userRepository.save(user);
  }

  private User getUserByUserName(String userName) {
    return userRepository.findOneByUsername(userName).orElseThrow(() ->
        new EntityNotFoundException("User with username: {0} not found", userName));
  }

  private boolean passwordsMatch(String oldPassword, String currentPasswordEncoded) {
    return new BCryptPasswordEncoder().matches(oldPassword, currentPasswordEncoded);
  }

}
