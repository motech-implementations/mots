package org.motechproject.mots.web;

import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.dto.AssignedModulesDto;
import org.motechproject.mots.mapper.ModuleAssignmentMapper;
import org.motechproject.mots.service.ModuleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ModuleAssignmentController extends BaseController {

  @Autowired
  private ModuleAssignmentService moduleAssignmentService;

  private ModuleAssignmentMapper moduleAssignmentMapper = ModuleAssignmentMapper.INSTANCE;

  /**
   * Assign modules for CHW.
   * @param assignedModulesDto dto with chw id and list of modules assigned to it
   */
  @RequestMapping(value = "/assignModules", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void assignModules(@RequestBody AssignedModulesDto assignedModulesDto) {
    AssignedModules assignedModules = moduleAssignmentMapper.fromDto(assignedModulesDto);

    moduleAssignmentService.assignModules(assignedModules);
  }
}
