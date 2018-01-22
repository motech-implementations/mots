package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.dto.ModuleSimpleDto;
import org.motechproject.mots.mapper.ModuleMapper;
import org.motechproject.mots.service.ModuleService;
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
public class ModuleController extends BaseController {

  @Autowired
  private ModuleService moduleService;

  private ModuleMapper moduleMapper = ModuleMapper.INSTANCE;

  /**
   * Get list of Module Simple DTOs.
   * @return list of all Modules
   */
  @RequestMapping(value = "/modules/simple", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModuleSimpleDto> getSimpleModules() {
    Iterable<Module> modules = moduleService.getModules();

    return moduleMapper.toSimpleDtos(modules);
  }

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

  /**
   * Create Module.
   * @param moduleDto DTO of Module to create
   * @return created Module
   */
  @RequestMapping(value = "/modules", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public ModuleDto createModule(@RequestBody ModuleDto moduleDto) {
    Module module = moduleService.createModule(moduleDto);

    return moduleMapper.toDto(module);
  }

  /**
   * Update Module.
   * @param id id of Module to update
   * @param moduleDto DTO of Module to update
   * @return updated Module
   */
  @RequestMapping(value = "/modules/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ModuleDto updateModule(@PathVariable("id") UUID id, @RequestBody ModuleDto moduleDto) {
    Module module = moduleService.updateModule(id, moduleDto);

    return moduleMapper.toDto(module);
  }

  /**
   * Release Module.
   * @param id id of Module to release
   */
  @RequestMapping(value = "/modules/{id}/release", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void releaseModule(@PathVariable("id") UUID id) {
    moduleService.releaseModule(id);
  }
}
