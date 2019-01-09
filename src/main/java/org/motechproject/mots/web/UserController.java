package org.motechproject.mots.web;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.RegistrationToken;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.PermissionDto;
import org.motechproject.mots.dto.RegistrationTokenDto;
import org.motechproject.mots.dto.RoleDto;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.dto.UserProfileDto;
import org.motechproject.mots.mapper.PermissionMapper;
import org.motechproject.mots.mapper.RegistrationTokenMapper;
import org.motechproject.mots.mapper.RoleMapper;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.repository.RegistrationTokenRepository;
import org.motechproject.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController extends BaseController {

  public static final String ROLE_PARAM = "role";

  @Autowired
  private UserService userService;

  @Autowired
  private RegistrationTokenRepository registrationTokenRepository;

  private UserMapper userMapper = UserMapper.INSTANCE;

  private RoleMapper roleMapper = RoleMapper.INSTANCE;

  private PermissionMapper permissionMapper = PermissionMapper.INSTANCE;

  private RegistrationTokenMapper registrationTokenMapper = RegistrationTokenMapper.INSTANCE;

  /**
   * Get list of users.
   * @return list of all users
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserDto> getUsers() {
    Iterable<User> users = userService.getUsers();

    return userMapper.toDtos(users);
  }

  /**
   * Finds users matching all of the provided parameters.
   * If there are no parameters, return all users.
   */
  @RequestMapping(value = "/user/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<UserDto> searchUsers(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = ROLE_PARAM, required = false) String role,
      Pageable pageable) throws IllegalArgumentException {

    Page<User> users = userService.searchUsers(username, email, name, role, pageable);
    List<UserDto> userDtos = userMapper.toDtos(users.getContent());

    return new PageImpl<>(userDtos, pageable, users.getTotalElements());
  }

  /**
   * Get User with given id.
   * @param id id of User to find
   * @return User with given id
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserDto getUser(@PathVariable("id") UUID id) {
    User user = userService.getUser(id);

    return userMapper.toDto(user);
  }

  /**
   * Create User.
   * @param userDto DTO of User to be created
   * @return created User
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDto createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    User user = userMapper.fromDto(userDto);
    return userMapper.toDto(userService.registerNewUser(user));
  }

  /**
   * Create a user with a registration token.
   * @param userDto the user data
   * @return created user
   */
  @RequestMapping(value = "/register/{token}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserDto createUser(@RequestBody @Valid UserDto userDto,
      BindingResult bindingResult, @PathVariable("token") String key) {
    Optional<RegistrationToken> token = registrationTokenRepository.findByToken(key);
    RegistrationToken registrationToken = token.get();
    if (!registrationToken.isExpired()) {
      checkBindingResult(bindingResult);

      User user = userMapper.fromDto(userDto);
      user.setEmail(registrationToken.getEmail());
      user.setRoles(new HashSet<>(registrationToken.getRoles()));
      user.setIncharge(registrationToken.getIncharge());
      user = userService.createUser(user);

      registrationTokenRepository.delete(registrationToken);

      return userMapper.toDto(user);
    } else {
      registrationTokenRepository.delete(registrationToken);
      return null;
    }
  }

  /**
   * Update User.
   * @param id id of User to update
   * @param userDto DTO of User to be updated
   * @return updated User
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserDto saveUser(@PathVariable("id") UUID id, @RequestBody @Valid UserDto userDto,
      BindingResult bindingResult) {

    checkBindingResult(bindingResult);
    boolean encodeNewPassword = true;

    if (StringUtils.isEmpty(userDto.getPassword())) {
      encodeNewPassword = false;
    }

    User existingUser = userService.getUser(id);
    userMapper.passwordNullSafeUpdateFromDto(userDto, existingUser);

    return userMapper.toDto(userService.saveUser(existingUser, encodeNewPassword));
  }

  /**
   * Update User password.
   * @param oldPassword user previous password, should be the same as current password
   * @param newPassword user's new password
   */
  @RequestMapping(value = "/user/passwordchange",
      method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String changeCurrentUserPassword(@RequestParam("oldPassword") String oldPassword,
      @RequestParam("newPassword") String newPassword, Principal principal) {
    userService.changeUserPassword(principal.getName(), newPassword, oldPassword);

    return "Password changed successfully.";
  }

  /**
   * Update profile information about User.
   * @param id id of User to update Profile
   * @param userProfileDto DTO of User Profile to be updated
   * @return updated User Profile
   */
  @RequestMapping(value = "/user/profile/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserProfileDto saveUserProfile(@PathVariable("id") UUID id,
      @RequestBody @Valid UserProfileDto userProfileDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    final User updatedUserProfile = userService.editUserProfile(id, userProfileDto);

    return userMapper.toUserProfileDto(updatedUserProfile);
  }

  /**
   * Get info about current logged-in User.
   */
  @RequestMapping(value = "/user/profile",
      method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserProfileDto getUserProfile(Principal principal) {
    User user = userService.getUserByUserName(principal.getName());
    return userMapper.toUserProfileDto(user);
  }

  /**
   * Get list of roles.
   * @return list of all roles
   */
  @RequestMapping(value = "/role", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<RoleDto> getRoles() {
    Iterable<UserRole> roles = userService.getRoles();

    return roleMapper.toDtos(roles);
  }

  /**
   * Get list of permissions.
   * @return list of all permissions
   */
  @RequestMapping(value = "/permission", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PermissionDto> getPermissions() {
    Iterable<UserPermission> permissions = userService.getPermissions();

    return permissionMapper.toDtos(permissions);
  }

  /**
   * Get Role with given id.
   * @param id id of Role to find
   * @return Role with given id
   */
  @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoleDto getRole(@PathVariable("id") UUID id) {

    UserRole role = userService.getRole(id);
    return roleMapper.toDto(role);
  }

  /**
   * Finds roles matching all of the provided parameters.
   * If there are no parameters, return all roles.
   * @param name name of the role
   * @param pageable pagination parameters (page size, page number, sort order)
   * @return page with roles matching provided parameters
   */
  @RequestMapping(value = "/role/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<RoleDto> searchRoles(@RequestParam(value = "name", required = false) String name,
      Pageable pageable) throws IllegalArgumentException {

    Page<UserRole> roles = userService.searchRoles(name, pageable);
    List<RoleDto> roleDtos = roleMapper.toDtos(roles.getContent());

    return new PageImpl<>(roleDtos, pageable, roles.getTotalElements());
  }

  /**
   * Create Role.
   * @param roleDto DTO of Role to be created
   * @return created Role
   */
  @RequestMapping(value = "/role", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoleDto createRole(@RequestBody @Valid RoleDto roleDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    UserRole role = roleMapper.fromDto(roleDto);
    return roleMapper.toDto(userService.saveRole(role));
  }

  /**
   * Update Role.
   * @param id id of Role to update
   * @param roleDto DTO of Role to be updated
   * @return updated Role
   */
  @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoleDto saveRole(@PathVariable("id") UUID id, @RequestBody @Valid RoleDto roleDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    UserRole role = userService.getRole(id);

    if (role.getReadonly()) {
      throw new IllegalArgumentException("Readonly role cannot be edited");
    }

    roleMapper.updateFromDto(roleDto, role);
    return roleMapper.toDto(userService.saveRole(role));
  }

  /**
   * Get Registration Token with given key.
   * @param key key of Registration Token to find
   * @return Registration Token with given key
   */
  @RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RegistrationTokenDto getToken(@PathVariable("token") String key) {

    Optional<RegistrationToken> token = registrationTokenRepository.findByToken(key);
    if (token.isPresent()) {
      RegistrationToken registrationToken = token.get();
      if (registrationToken.isExpired()) {
        registrationTokenRepository.delete(registrationToken);
      } else {
        return registrationTokenMapper.toDto(registrationToken);
      }
    }
    return null;
  }
}
