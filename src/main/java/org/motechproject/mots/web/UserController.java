package org.motechproject.mots.web;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.PermissionDto;
import org.motechproject.mots.dto.RoleDto;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.dto.UserProfileDto;
import org.motechproject.mots.mapper.PermissionMapper;
import org.motechproject.mots.mapper.RoleMapper;
import org.motechproject.mots.mapper.UserMapper;
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

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private static final RoleMapper ROLE_MAPPER = RoleMapper.INSTANCE;

  private static final PermissionMapper PERMISSION_MAPPER = PermissionMapper.INSTANCE;

  @Autowired
  private UserService userService;

  /**
   * Get list of users.
   *
   * @return list of all users
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserDto> getUsers() {
    Iterable<User> users = userService.getUsers();

    return USER_MAPPER.toDtos(users);
  }

  /**
   * Finds users matching all of the provided parameters.
   * If there are no parameters, return all users.
   *
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param name name of a user
   * @param email email of a user
   * @param role name of role of a user
   * @param username username of user
   * @return page with found users
   */
  @RequestMapping(value = "/user/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<UserDto> searchUsers(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = ROLE_PARAM, required = false) String role,
      Pageable pageable) {

    Page<User> users = userService.searchUsers(username, email, name, role, pageable);
    List<UserDto> userDtos = USER_MAPPER.toDtos(users.getContent());

    return new PageImpl<>(userDtos, pageable, users.getTotalElements());
  }

  /**
   * Get User with given id.
   *
   * @param id id of User to find
   * @return User with given id
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserDto getUser(@PathVariable("id") UUID id) {
    User user = userService.getUser(id);

    return USER_MAPPER.toDto(user);
  }

  /**
   * Create User.
   *
   * @param userDto DTO of User to be created
   * @param bindingResult spring object used for validation
   * @return created User
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDto createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {

    checkBindingResult(bindingResult);

    User user = USER_MAPPER.fromDto(userDto);
    return USER_MAPPER.toDto(userService.registerNewUser(user));
  }

  /**
   * Update User.
   *
   * @param id id of User to update
   * @param userDto DTO of User to be updated
   * @param bindingResult spring object used for validation
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
    USER_MAPPER.passwordNullSafeUpdateFromDto(userDto, existingUser);

    return USER_MAPPER.toDto(userService.saveUser(existingUser, encodeNewPassword));
  }

  /**
   * Update User password.
   *
   * @param oldPassword user previous password, should be the same as current password
   * @param newPassword user's new password
   * @param principal java security object
   * @return success message
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
   *
   * @param id id of User to update Profile
   * @param userProfileDto DTO of User Profile to be updated
   * @param bindingResult spring object used for validation
   * @return updated User Profile
   */
  @RequestMapping(value = "/user/profile/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserProfileDto saveUserProfile(@PathVariable("id") UUID id,
      @RequestBody @Valid UserProfileDto userProfileDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    final User updatedUserProfile = userService.editUserProfile(id, userProfileDto);

    return USER_MAPPER.toUserProfileDto(updatedUserProfile);
  }

  /**
   * Get info about current logged-in User.
   *
   * @param principal java security object
   * @return dto of user profile
   */
  @RequestMapping(value = "/user/profile",
      method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserProfileDto getUserProfile(Principal principal) {
    User user = userService.getUserByUserName(principal.getName());
    return USER_MAPPER.toUserProfileDto(user);
  }

  /**
   * Get list of roles.
   *
   * @return list of all roles
   */
  @RequestMapping(value = "/role", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<RoleDto> getRoles() {
    Iterable<UserRole> roles = userService.getRoles();

    return ROLE_MAPPER.toDtos(roles);
  }

  /**
   * Get list of permissions.
   *
   * @return list of all permissions
   */
  @RequestMapping(value = "/permission", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PermissionDto> getPermissions() {
    Iterable<UserPermission> permissions = userService.getPermissions();

    return PERMISSION_MAPPER.toDtos(permissions);
  }

  /**
   * Get {@link UserRole} with given id.
   *
   * @param id id of Role to find
   * @return Role with given id
   */
  @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoleDto getRole(@PathVariable("id") UUID id) {

    UserRole role = userService.getRole(id);
    return ROLE_MAPPER.toDto(role);
  }

  /**
   * Finds roles matching all of the provided parameters.
   * If there are no parameters, return all roles.
   *
   * @param name name of the role
   * @param pageable pagination parameters (page size, page number, sort order)
   * @return page with roles matching provided parameters
   */
  @RequestMapping(value = "/role/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<RoleDto> searchRoles(@RequestParam(value = "name", required = false) String name,
      Pageable pageable) {

    Page<UserRole> roles = userService.searchRoles(name, pageable);
    List<RoleDto> roleDtos = ROLE_MAPPER.toDtos(roles.getContent());

    return new PageImpl<>(roleDtos, pageable, roles.getTotalElements());
  }

  /**
   * Create Role from given dto.
   *
   * @param roleDto DTO of Role to be created
   * @param bindingResult spring object used for validation
   * @return created Role
   */
  @RequestMapping(value = "/role", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoleDto createRole(@RequestBody @Valid RoleDto roleDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    UserRole role = ROLE_MAPPER.fromDto(roleDto);
    return ROLE_MAPPER.toDto(userService.saveRole(role));
  }

  /**
   * Update {@link UserRole} from given dto.
   *
   * @param id id of Role to update
   * @param roleDto DTO of Role to be updated
   * @param bindingResult spring object used for validation
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

    ROLE_MAPPER.updateFromDto(roleDto, role);
    return ROLE_MAPPER.toDto(userService.saveRole(role));
  }
}
