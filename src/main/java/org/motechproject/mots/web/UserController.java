package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.mapper.UserMapper;
import org.motechproject.mots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  public UserDto getUser(UUID id) {
    User user = userService.getUser(id);

    return userMapper.toDto(user);
  }

  /**
   * Creates User.
   * @param userDto DTO of User to be created
   * @return created User
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDto createUser(@RequestBody UserDto userDto) {
    User user = userMapper.fromDto(userDto);

    return userMapper.toDto(userService.saveUser(user));
  }

  /**
   * Updates User.
   * @param id id of User to update
   * @param userDto DTO of User to be updated
   * @return updated User
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDto saveUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
    User user = userMapper.fromDto(userDto);

    return userMapper.toDto(userService.saveUser(user));
  }


}
