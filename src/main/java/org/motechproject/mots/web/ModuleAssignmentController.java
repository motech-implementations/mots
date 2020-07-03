package org.motechproject.mots.web;

import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.dto.ChwModulesDto;
import org.motechproject.mots.dto.DistrictAssignmentDto;
import org.motechproject.mots.dto.GroupAssignmentDto;
import org.motechproject.mots.dto.ModuleAssignmentDto;
import org.motechproject.mots.mapper.ModuleAssignmentMapper;
import org.motechproject.mots.service.ModuleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ModuleAssignmentController extends BaseController {

  @Autowired
  private ModuleAssignmentService moduleAssignmentService;

  @Autowired
  private ModuleAssignmentMapper moduleAssignmentMapper;

  /**
   * Assign modules for CHW.
   * @param moduleAssignmentDto dto with chw id and list of modules assigned to it
   */
  @RequestMapping(value = "/module/assign", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void assignModules(@RequestBody ModuleAssignmentDto moduleAssignmentDto) {
    moduleAssignmentService.assignModules(moduleAssignmentDto);
  }

  /**
   * Assign modules to all CHWs in a district.
   * @param districtAssignmentDto dto with district id, list of modules assigned to it
   *     and start and end dates
   */
  @RequestMapping(value = "/module/district/assign", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public boolean assignModulesToDistrict(
      @RequestBody @Valid DistrictAssignmentDto districtAssignmentDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    return moduleAssignmentService.assignModulesToChwsInLocation(districtAssignmentDto);
  }

  /**
   * Assign modules to all CHWs in a group.
   * @param groupAssignmentDto dto with group id, list of modules assigned to it
   *     and start and end dates
   */
  @RequestMapping(value = "/module/group/assign", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public boolean assignModulesToGroup(
      @RequestBody @Valid GroupAssignmentDto groupAssignmentDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    return moduleAssignmentService.assignModulesToGroup(groupAssignmentDto);
  }

  /**
   * Get Modules assigned to CHW.
   * @param chwId id of CHW
   * @return modules assigned to CHW
   */
  @RequestMapping(value = "/assignedModules", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ChwModulesDto getAssignedModules(@RequestParam("chwId") String chwId) {
    AssignedModules assignedModules =
        moduleAssignmentService.getAssignedModules(UUID.fromString(chwId));

    return moduleAssignmentMapper.toDto(assignedModules);
  }
}
