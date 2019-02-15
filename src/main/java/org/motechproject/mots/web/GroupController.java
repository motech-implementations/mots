package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.Group;
import org.motechproject.mots.dto.GroupDto;
import org.motechproject.mots.mapper.GroupMapper;
import org.motechproject.mots.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class GroupController extends BaseController {

  @Autowired
  private GroupService groupService;

  private GroupMapper groupMapper = GroupMapper.INSTANCE;

  /**
   * Get list of groups.
   * @return list of all groups
   */
  @RequestMapping(value = "/group", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<GroupDto> getGroups() {
    Iterable<Group> groups = groupService.getGroups();

    return groupMapper.toDtos(groups);
  }

  /**
   * Get group with given id.
   * @param id id of Group to find
   * @return Group with given id
   */
  @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public GroupDto getGroup(@PathVariable("id") UUID id) {
    Group group = groupService.getGroup(id);

    return groupMapper.toDto(group);
  }

  /**
   * Create Group.
   * @param groupDto DTO of Group to create
   * @return created Group
   */
  @RequestMapping(value = "/group", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public GroupDto createGroup(@RequestBody @Valid GroupDto groupDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    Group group = groupMapper.fromDto(groupDto);

    return groupMapper.toDto(groupService.saveGroup(group));
  }

  /**
   * Update Group.
   * @param id id of Group to update
   * @param groupDto DTO of Group to update
   * @return updated Group
   */
  @RequestMapping(value = "/group/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public GroupDto saveGroup(@PathVariable("id") UUID id,
      @RequestBody @Valid GroupDto groupDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    Group existingGroup = groupService.getGroup(id);
    groupMapper.updateFromDto(groupDto, existingGroup);

    return groupMapper.toDto(groupService.saveGroup(existingGroup));
  }

}
