package org.motechproject.mots.service;

import java.util.Iterator;
import java.util.UUID;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.mapper.ModuleMapper;
import org.motechproject.mots.repository.CourseRepository;
import org.motechproject.mots.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private CourseRepository courseRepository;

  private ModuleMapper moduleMapper = ModuleMapper.INSTANCE;

  @PreAuthorize(RoleNames.HAS_ASSIGN_MODULES_OR_MANAGE_MODULES_ROLE)
  public Iterable<Module> getModules() {
    return getCourse().getModules();
  }

  /**
   * Create new Module.
   * @param moduleDto DTO of Module to be created
   * @return Created Module
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Module createModule(ModuleDto moduleDto) {
    Module module = Module.initialize();
    moduleMapper.updateModuleFromDto(moduleDto, module);

    Course course = getCourse();
    module.setCourse(course);

    return moduleRepository.save(module);
  }

  /**
   * Update existing Module.
   * @param id id of Module to be updated
   * @param moduleDto Module DTO
   * @return updated Module
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Module updateModule(UUID id, ModuleDto moduleDto) {
    Module module = findById(id);

    if (!Status.DRAFT.equals(module.getStatus())) {
      throw new MotsException("Only Module draft can be updated");
    }

    moduleMapper.updateModuleFromDto(moduleDto, module);

    return moduleRepository.save(module);
  }

  /**
   * Release Module.
   * @param module Module to be released
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Module releaseModule(Module module) {
    module.release();

    return moduleRepository.save(module);
  }

  public Module findById(UUID id) {
    return moduleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        String.format("Module with id: %s not found", id.toString())));
  }

  private Course getCourse() {
    Iterable<Course> courses = courseRepository.findAll();
    Iterator<Course> courseIterator = courses.iterator();

    if (!courseIterator.hasNext()) {
      throw new EntityNotFoundException("No course exists");
    }

    return courseIterator.next();
  }
}
