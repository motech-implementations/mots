package org.motechproject.mots.service;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User getUser(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        String.format("User with id: %s not found", id.toString())));
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User saveUser(User user) {
    return userRepository.save(user);
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
}
