package org.motechproject.mots.service;

import static org.motechproject.mots.constants.DefaultPermissionConstants.MANAGE_ROLES;

import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.DefaultPermissionConstants;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.dto.UserProfileDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.repository.PermissionRepository;
import org.motechproject.mots.repository.RoleRepository;
import org.motechproject.mots.repository.UserRepository;
import org.motechproject.mots.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationHelper authenticationHelper;

  public User getUserByUserName(String userName) {
    return userRepository.findOneByUsername(userName).orElseThrow(() ->
        new EntityNotFoundException("User with username: {0} not found", userName));
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_ROLE)
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Finds {@link User} matching all of the provided parameters. If there are no parameters,
   *        return all users.
   *
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param email email of user
   * @param role name of role of user
   * @param username username of user
   * @param name name of user
   * @return page with found users.
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_ROLE)
  public Page<User> searchUsers(String username, String email, String name, String role,
      Pageable pageable) {
    return userRepository.search(username, email, name, role, pageable);
  }

  /**
   * Returns UserRoles.
   * @return user roles
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_OR_MANAGE_ROLES_ROLE)
  public Iterable<UserRole> getRoles() {
    User user = authenticationHelper.getCurrentUser();
    Set<UserPermission> permissions = user.getRoles().stream()
        .flatMap(r -> r.getPermissions().stream()).collect(Collectors.toSet());

    Iterable<UserRole> roles = roleRepository.findAll();

    if (authenticationHelper.isAdminUser()
        || permissions.stream().anyMatch(p -> p.getName().equals(MANAGE_ROLES))) {
      return roles;
    }

    return StreamSupport.stream(roles.spliterator(), false)
        .filter(role -> user.getRoles().stream().anyMatch(r -> role.getId().equals(r.getId()))
            || role.getPermissions().stream().allMatch(permission -> permissions.stream()
            .anyMatch(p -> p.getId().equals(permission.getId()))))
        .collect(Collectors.toList());
  }

  /**
   * Returns UserPermissions.
   * @return user permissions
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_ROLES_ROLE)
  public Iterable<UserPermission> getPermissions() {
    return permissionRepository.findAll();
  }

  /**
   * Gets {@link UserRole} with given id.
   *
   * @param id id of a role
   * @return UserRole or throws error if it doesn't exists
   * @throws EntityNotFoundException if role with id not found
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_ROLES_ROLE)
  public UserRole getRole(UUID id) {
    return roleRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Role with id: {0} not found", id.toString()));
  }

  /**
   * Saves a UserRole. Checks if role has id assigned (ie. if it is an update action),
   * if not it checks for name uniqueness. If the name is not unique it throws MotsException.
   *
   * @param role UserRole to be created or updated.
   * @return saved UserRole
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_ROLES_ROLE)
  public UserRole saveRole(UserRole role) {
    roleRepository.findByName(role.getName()).ifPresent(r -> {
      if (role.getId() == null || !role.getId().equals(r.getId())) {
        throw new MotsException(MessageFormat.format("The role with {0} name already exists. "
        + "Change the name.", r.getName()));
      }
    });
    return roleRepository.save(role);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_ROLES_ROLE)
  public Page<UserRole> searchRoles(String name, Pageable pageable) {
    return roleRepository.search(name, pageable);
  }

  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_ROLE)
  public User getUser(UUID id) {
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("User with id: {0} not found", id.toString()));
  }

  /**
   * Save User with new encoded password (if it's not blank).
   *
   * @param id id of User to update
   * @param userDto DTO of User to be updated
   * @return saved User
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_ROLE)
  public UserDto saveUser(UUID id, UserDto userDto) {
    User user = getUser(id);

    if (!checkIfCanModifyUserWithRoles(user.getRoles())) {
      throw new MotsException("You don't have permission to edit this user");
    }

    USER_MAPPER.updateFromDto(userDto, user);

    if (!checkIfCanModifyUserWithRoles(user.getRoles())) {
      throw new MotsException("You cannot give user more permissions than you have,"
          + " choose different role for the user");
    }

    if (StringUtils.isNotBlank(userDto.getPassword())) {
      String newPasswordEncoded = passwordEncoder.encode(userDto.getPassword());
      user.setPassword(newPasswordEncoded);
    }

    return USER_MAPPER.toDto(userRepository.save(user));
  }

  /**
   * Create User with new encoded password.
   *
   * @param user User to be created.
   * @return created User
   */
  @PreAuthorize(DefaultPermissionConstants.HAS_MANAGE_USERS_ROLE)
  public User registerNewUser(User user) {
    if (!checkIfCanModifyUserWithRoles(user.getRoles())) {
      throw new MotsException("You cannot create User with more permissions than you,"
          + " choose different role for the user");
    }

    String newPasswordEncoded = passwordEncoder.encode(user.getPassword());
    user.setPassword(newPasswordEncoded);

    return userRepository.save(user);
  }

  /**
   * Save User Profile with new encoded password (if it's not blank).
   *
   * @param userProfileDto User Profile to be updated.
   * @param userId id of user to update
   * @return saved User
   */
  public User editUserProfile(UUID userId, final UserProfileDto userProfileDto) {
    User existingUser = getUser(userId);
    final boolean encodeNewPassword = !StringUtils.isEmpty(userProfileDto.getPassword());

    if (encodeNewPassword) {
      changeUserPassword(existingUser.getUsername(),
          userProfileDto.getNewPassword(), userProfileDto.getPassword());
    }
    USER_MAPPER.updateFromUserProfileDto(userProfileDto, existingUser);

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

    String newPasswordEncoded = passwordEncoder.encode(newPassword);
    user.setPassword(newPasswordEncoded);
    userRepository.save(user);
  }

  private boolean passwordsMatch(String oldPassword, String currentPasswordEncoded) {
    return passwordEncoder.matches(oldPassword, currentPasswordEncoded);
  }

  private boolean checkIfCanModifyUserWithRoles(Set<UserRole> roles) {
    if (authenticationHelper.isAdminUser()) {
      return true;
    }

    User user = authenticationHelper.getCurrentUser();
    Set<UserPermission> permissions = user.getRoles().stream()
        .flatMap(r -> r.getPermissions().stream()).collect(Collectors.toSet());

    if (permissions.stream().anyMatch(p -> p.getName().equals(MANAGE_ROLES))) {
      return true;
    }

    for (UserRole role : roles) {
      if (user.getRoles().stream().noneMatch(r -> r.getId().equals(role.getId()))) {
        UserRole roleToCheck = getRole(role.getId());

        for (UserPermission permission : roleToCheck.getPermissions()) {
          if (permissions.stream().noneMatch(p -> p.getId().equals(permission.getId()))) {
            return false;
          }
        }
      }
    }

    return true;
  }
}
