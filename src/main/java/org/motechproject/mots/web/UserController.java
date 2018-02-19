package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.RoleDto;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.mapper.RoleMapper;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.service.UserService;
import org.motechproject.mots.validate.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController extends BaseController {

  @Autowired
  private UserService userService;

  private UserMapper userMapper = UserMapper.INSTANCE;

  private RoleMapper roleMapper = RoleMapper.INSTANCE;

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
  public UserDto createUser(@RequestBody UserDto userDto) {
    User user = userMapper.fromDto(userDto);

    return userMapper.toDto(userService.registerNewUser(user));
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
  public UserDto saveUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
    User existingUser = userService.getUser(id);
    userMapper.updateFromDto(userDto, existingUser);

    return userMapper.toDto(userService.saveUser(existingUser));
  }

  /**
   * Update User password.
   * @param currentPassword user previous password, should be the same as current password
   * @param newPassword user's new password
   * @return updated User
   */
  @RequestMapping(value = "/user/password/change/{currentPassword}/{newPassword}",
      method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<String> changeCurrentUserPassword(
      @PathVariable("currentPassword") String currentPassword,
      @PathVariable("newPassword") String newPassword) {

    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User existingUser = userService.getUserByUserName(userName);

    if (existingUser == null
        || !PasswordValidator.validateCurrentPassword(
            existingUser.getPassword(), currentPassword)) {
      return new ResponseEntity<>("Current password is incorrect.", HttpStatus.BAD_REQUEST);
    }

    userService.changeUserPassword(existingUser, newPassword);

    return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
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
}
