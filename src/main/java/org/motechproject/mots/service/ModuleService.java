package org.motechproject.mots.service;

import java.util.Iterator;
import java.util.UUID;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.domain.CourseModule;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.dto.CourseDto;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.mapper.ModuleMapper;
import org.motechproject.mots.repository.CourseModuleRepository;
import org.motechproject.mots.repository.CourseRepository;
import org.motechproject.mots.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CourseModuleRepository courseModuleRepository;

  private ModuleMapper moduleMapper = ModuleMapper.INSTANCE;

  @PreAuthorize(RoleNames.HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE)
  public Iterable<Module> getModules() {
    return getCourse().getModules();
  }

  @PreAuthorize(RoleNames.HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE)
  public Iterable<Course> getCourses() {
    return courseRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE)
  public Iterable<Module> getReleasedModules() {
    return getReleasedCourse().getReleasedModules();
  }

  /**
   * Create new Module.
   * @param moduleDto DTO of Module to be created
   * @return Created Module
   */
  @Transactional
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Module createModule(ModuleDto moduleDto) {
    Module module = Module.initialize();
    moduleMapper.updateModuleFromDto(moduleDto, module);

    module = moduleRepository.save(module);
    Course course = getCourse();
    CourseModule courseModule = new CourseModule(course, module, course.getModules().size());
    courseModuleRepository.save(courseModule);

    return module;
  }

  /**
   * Update existing Module.
   * @param id id of Module to be updated
   * @param moduleDto Module DTO
   * @return updated Module
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Module updateModule(UUID id, ModuleDto moduleDto) {
    Module module = findModuleById(id);

    if (!Status.DRAFT.equals(module.getStatus())) {
      throw new MotsException("Only Module draft can be updated");
    }

    moduleMapper.updateModuleFromDto(moduleDto, module);

    return moduleRepository.save(module);
  }

  /**
   * Create new Course.
   * @param courseDto DTO of Course to be created
   * @return Created Course
   */
  @Transactional
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Course createCourse(CourseDto courseDto) {
    Course course = Course.initialize();
    moduleMapper.updateCourseFromDto(courseDto, course);

    return courseRepository.save(course);
  }

  /**
   * Update existing Course.
   * @param id id of Course to be updated
   * @param courseDto Course DTO
   * @return updated Course
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Course updateCourse(UUID id, CourseDto courseDto) {
    Course course = findCourseById(id);

    if (!Status.DRAFT.equals(course.getStatus())) {
      throw new MotsException("Only Course draft can be updated");
    }

    moduleMapper.updateCourseFromDto(courseDto, course);

    return courseRepository.save(course);
  }

  /**
   * Release Course.
   * @param course Course to be released
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Course releaseCourse(Course course) {
    return course;
  }

  public Module findModuleById(UUID id) {
    return moduleRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Module with id: {0} not found", id.toString()));
  }

  public Course findCourseById(UUID id) {
    return courseRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Course with id: {0} not found", id.toString()));
  }

  private Course getCourse() {
    Iterable<Course> courses = courseRepository.findAll();
    Iterator<Course> courseIterator = courses.iterator();

    if (!courseIterator.hasNext()) {
      throw new EntityNotFoundException("No course exists");
    }

    return courseIterator.next();
  }

  private Course getReleasedCourse() {
    return courseRepository.findByStatus(Status.RELEASED);
  }
}
