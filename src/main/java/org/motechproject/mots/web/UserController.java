package org.motechproject.mots.web;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.dto.RoleDto;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.mapper.RoleMapper;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
