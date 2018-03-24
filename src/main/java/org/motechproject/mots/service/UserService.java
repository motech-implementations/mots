package org.motechproject.mots.service;

import static org.motechproject.mots.constants.MotsConstants.INCHARGE_USER_ROLE;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.UserProfileDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsAccessDeniedException;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.repository.RoleRepository;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

  @Autowired
  AuthenticationHelper authenticationHelper;

  private UserMapper userMapper = UserMapper.INSTANCE;

  public User getUserByUserName(String userName) {
    return userRepository.findOneByUsername(userName).orElseThrow(() ->
        new EntityNotFoundException("User with username: {0} not found", userName));
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_ROLE)
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Finds users matching all of the provided parameters. If there are no parameters, return all
   * users.
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE)
  public Page<User> searchUsers(String username, String email, String name, String role,
      Pageable pageable) throws IllegalArgumentException {
    if (authenticationHelper.getCurrentUser().hasPermission(UserPermission.MANAGE_USERS)) {
      return userRepository.search(username, email, name, role, pageable);
    }

    final Optional<UserRole> incharge = getInchargeUserRole();
    if (incharge.isPresent()) {
      return userRepository.search(username, email, name, incharge.get().getName(), pageable);
    }

    return new PageImpl<>(Collections.EMPTY_LIST);
  }

  /**
   * Returns UserRoles.
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE)
  public Iterable<UserRole> getRoles() {
    if (authenticationHelper.getCurrentUser().hasPermission(UserPermission.MANAGE_USERS)) {
      return roleRepository.findAll();
    }

    final Optional<UserRole> incharge = getInchargeUserRole();
    if (incharge.isPresent()) {
      return Collections.singleton(incharge.get());
    }

    return Collections.EMPTY_LIST;
  }

  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE)
  public User getUser(UUID id) {
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("User with id: {0} not found", id.toString()));
  }

  private Optional<UserRole> getInchargeUserRole() {
    return roleRepository.findByName(INCHARGE_USER_ROLE);
  }

  /**
   * Save User with new encoded password (if it's not blank).
   *
   * @param user User to be created.
   * @return saved User
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE)
  public User saveUser(User user, boolean encodeNewPassword) {
    if (encodeNewPassword) {
      String newPasswordEncoded = new BCryptPasswordEncoder().encode(user.getPassword());
      user.setPassword(newPasswordEncoded);
    }

    return validAndSave(user);
  }

  /**
   * Create User with new encoded password.
   *
   * @param user User to be created.
   * @return created User
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_USERS_OR_MANAGE_INCHARGE_USERS_ROLE)
  public User registerNewUser(User user) {

    String newPasswordEncoded = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(newPasswordEncoded);

    return validAndSave(user);
  }

  private User validAndSave(User user) {
    if (authenticationHelper.getCurrentUser().hasPermission(UserPermission.MANAGE_INCHARGE_USERS)
        && !user.hasOnlyRole(getInchargeUserRole().get().getId())) {
      throw new MotsAccessDeniedException("You can manage only Incharge users");
    }

    return userRepository.save(user);
  }

  /**
   * Save User Profile with new encoded password (if it's not blank).
   *
   * @param userProfileDto User Profile to be updated.
   * @return saved User
   */
  public User editUserProfile(UUID userId, final UserProfileDto userProfileDto) {
    User existingUser = getUser(userId);
    final boolean encodeNewPassword = !StringUtils.isEmpty(userProfileDto.getPassword());

    if (encodeNewPassword) {
      changeUserPassword(existingUser.getUsername(),
          userProfileDto.getNewPassword(), userProfileDto.getPassword());
    }
    userMapper.updateFromUserProfileDto(userProfileDto, existingUser);

    return userRepository.save(existingUser);
  }

  /**
   * Updates user's password.
   *
   * @param username of user which password is about to change.
   * @param newPassword is new password value for user.
   * @param currentPassword is current user's password.
   */
  public void changeUserPassword(String username, String newPassword, String currentPassword) {
    User user = getUserByUserName(username);

    if (!passwordsMatch(currentPassword, user.getPassword())) {
      throw new IllegalArgumentException("Current password is incorrect.");
    }

    String newPasswordEncoded = new BCryptPasswordEncoder().encode(newPassword);
    user.setPassword(newPasswordEncoded);
    userRepository.save(user);
  }

  private boolean passwordsMatch(String oldPassword, String currentPasswordEncoded) {
    return new BCryptPasswordEncoder().matches(oldPassword, currentPasswordEncoded);
  }
}
