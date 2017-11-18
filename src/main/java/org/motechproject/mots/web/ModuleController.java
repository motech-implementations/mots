package org.motechproject.mots.web;

import java.util.List;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.mapper.ModuleMapper;
import org.motechproject.mots.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ModuleController extends BaseController {

  @Autowired
  private ModuleService moduleService;

  private ModuleMapper moduleMapper = ModuleMapper.INSTANCE;

  /**
   * Get list of Modules.
   * @return list of all Modules
   */
  @RequestMapping(value = "/modules", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModuleDto> getModules() {
    Iterable<Module> modules = moduleService.getModules();

    return moduleMapper.toDtos(modules);
  }
}
