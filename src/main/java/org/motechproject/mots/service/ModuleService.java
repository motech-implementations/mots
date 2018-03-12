package org.motechproject.mots.service;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CourseModuleRepository courseModuleRepository;

  @Autowired
  private ModuleAssignmentService moduleAssignmentService;

  private ModuleMapper moduleMapper = ModuleMapper.INSTANCE;

  @PreAuthorize(RoleNames.HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE)
  public List<Course> getCourses() {
    return courseRepository.findAllByOrderByVersionAsc();
  }

  /**
   * Get modules from released course.
   * @return released modules
   */
  @PreAuthorize(RoleNames.HAS_ASSIGN_OR_DISPLAY_OR_MANAGE_MODULES_ROLE)
  public Iterable<Module> getReleasedModules() {
    Course course = getReleasedCourseIfExists();

    if (course == null) {
      return new ArrayList<>();
    }

    return course.getModules();
  }

  /**
   * Create new Module.
   * @param moduleDto DTO of Module to be created
   * @return Created Module
   */
  @Transactional
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public ModuleDto createModule(ModuleDto moduleDto) {
    Course course = getDraftCourse();
    CourseModule courseModule = new CourseModule(course, Module.initialize(),
        course.getModules().size());

    moduleMapper.updateCourseModuleFromDto(moduleDto, courseModule);

    courseModule = courseModuleRepository.save(courseModule);

    return moduleMapper.toDto(courseModule);
  }

  /**
   * Update existing Module.
   * @param id id of Module to be updated
   * @param moduleDto Module DTO
   * @return updated Module
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public ModuleDto updateModule(UUID id, ModuleDto moduleDto) {
    Course course = getDraftCourse();
    CourseModule courseModule = course.findCourseModuleByModuleId(id);

    Module module = courseModule.getModule();

    if (!Status.DRAFT.equals(module.getStatus())) {
      throw new MotsException("Only Module draft can be updated");
    }

    moduleMapper.updateCourseModuleFromDto(moduleDto, courseModule);

    courseModule = courseModuleRepository.save(courseModule);

    return moduleMapper.toDto(courseModule);
  }

  /**
   * Copy Module as a new draft.
   * @param id id of module to be copied
   * @return copied module draft
   */
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public ModuleDto copyModule(UUID id) {
    Course course = getDraftCourse();
    CourseModule courseModule = course.findCourseModuleByModuleId(id);

    if (!Status.RELEASED.equals(courseModule.getModule().getStatus())) {
      throw new MotsException("Only released Module can be copied");
    }

    Module moduleCopy = courseModule.getModule().copyAsNewDraft();
    courseModule.setModule(moduleCopy);

    courseModule = courseModuleRepository.save(courseModule);

    return moduleMapper.toDto(courseModule);
  }

  /**
   * Create new Course.
   * @return Created Course
   */
  @Transactional
  @PreAuthorize(RoleNames.HAS_MANAGE_MODULES_ROLE)
  public Course createCourse() {
    List<Course> courses = courseRepository.findByStatus(Status.DRAFT);

    if (courses != null && !courses.isEmpty()) {
      throw new MotsException("Only one draft course allowed at a time");
    }

    Course releasedCourse = getReleasedCourseIfExists();
    Course course;

    if (releasedCourse == null) {
      course = Course.initialize();
    } else {
      course = releasedCourse.copyAsNewDraft();
    }

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
    List<Module> newVersionModules = course.getNewVersionModules();
    course.release();

    moduleAssignmentService.unassignOldModulesVersions(newVersionModules);

    return courseRepository.save(course);
  }

  /**
   * Get released course IVR Id.
   * @return released course IVR Id
   */
  public String getReleasedCourseIvrId() {
    Course course = getReleasedCourse();
    return course.getIvrId();
  }

  public Course findCourseById(UUID id) {
    return courseRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Course with id: {0} not found", id.toString()));
  }

  /**
   * Find released Course Module for Module with given Id.
   * @param id if of related Module
   * @return Course Module with given Module
   */
  public CourseModule findReleasedCourseModuleByModuleId(UUID id) {
    Course course = getReleasedCourse();
    return course.findCourseModuleByModuleId(id);
  }

  /**
   * Get released Course.
   * @return Course with released status
   */
  public Course getReleasedCourse() {
    Course course = getReleasedCourseIfExists();

    if (course == null) {
      throw new EntityNotFoundException("No released course exists");
    }

    return course;
  }

  private Course getDraftCourse() {
    List<Course> courses = courseRepository.findByStatus(Status.DRAFT);

    if (courses == null || courses.isEmpty()) {
      throw new EntityNotFoundException("No draft course exists");
    }

    if (courses.size() > 1) {
      throw new MotsException("Too many draft course found");
    }

    return courses.get(0);
  }

  private Course getReleasedCourseIfExists() {
    List<Course> courses = courseRepository.findByStatus(Status.RELEASED);

    if (courses == null || courses.isEmpty()) {
      return null;
    }

    if (courses.size() > 1) {
      throw new MotsException("Too many released course found");
    }

    return courses.get(0);
  }
}
