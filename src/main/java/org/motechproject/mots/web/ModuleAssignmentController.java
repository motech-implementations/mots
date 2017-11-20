package org.motechproject.mots.web;

import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.dto.ChwModulesDto;
import org.motechproject.mots.dto.ModuleAssignmentDto;
import org.motechproject.mots.mapper.ModuleAssignmentMapper;
import org.motechproject.mots.service.ModuleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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

  private ModuleAssignmentMapper moduleAssignmentMapper = ModuleAssignmentMapper.INSTANCE;

  /**
   * Assign modules for CHW.
   * @param moduleAssignmentDto dto with chw id and list of modules assigned to it
   */
  @RequestMapping(value = "/assignModules", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void assignModules(@RequestBody ModuleAssignmentDto moduleAssignmentDto) {
    AssignedModules assignedModules = moduleAssignmentMapper.fromDto(moduleAssignmentDto);

    moduleAssignmentService.assignModules(assignedModules);
  }

  /**
   * Get Modules assigned to CHW.
   * @param chwId id of CHW
   * @return modules assigned to CHW
   */
  @RequestMapping(value = "/assignedModules", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ChwModulesDto getAssignedModules(@RequestParam(value = "chwId") String chwId) {
    AssignedModules assignedModules =
        moduleAssignmentService.getAssignedModules(UUID.fromString(chwId));

    return moduleAssignmentMapper.toDto(assignedModules);
  }
}
