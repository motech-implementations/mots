package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    return userRepository.findOne(id);
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public User saveUser(User user) {
    return userRepository.save(user);
  }
}
