package org.motechproject.mots.web;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.Course;
import org.motechproject.mots.dto.CourseDto;
import org.motechproject.mots.dto.ModuleDto;
import org.motechproject.mots.dto.ModuleSimpleDto;
import org.motechproject.mots.service.ModuleService;
import org.motechproject.mots.validate.CourseReleaseCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ModuleController extends BaseController {

  private static final String COURSE = "course";

  @Autowired
  private ModuleService moduleService;

  @Autowired
  private SmartValidator validator;

  /**
   * Get list of Module Simple DTOs.
   * @return list of all Modules
   */
  @RequestMapping(value = "/modules/simple", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModuleSimpleDto> getSimpleModules() {
    return moduleService.getReleasedModules();
  }

  /**
   * Create Module.
   * @param moduleDto DTO of Module to create
   * @return created Module
   */
  @RequestMapping(value = "/modules", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public ModuleDto createModule(@RequestBody @Valid ModuleDto moduleDto,
      BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    return moduleService.createModule(moduleDto);
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
  public ModuleDto updateModule(@PathVariable("id") UUID id,
      @RequestBody @Valid ModuleDto moduleDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    return moduleService.updateModule(id, moduleDto);
  }

  /**
   * Copy Module as a new draft.
   * @param id id of Module to update
   * @return copy of Module to be edited
   */
  @RequestMapping(value = "/modules/{id}/edit", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ModuleDto copyModule(@PathVariable("id") UUID id) {
    return moduleService.copyModule(id);
  }

  /**
   * Get list of Courses.
   * @return list of all Courses
   */
  @RequestMapping(value = "/courses", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<CourseDto> getCourses() {
    return moduleService.getCourses();
  }

  /**
   * Create Course.
   * @return created Course
   */
  @RequestMapping(value = "/courses", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CourseDto createCourse() {
    return moduleService.createCourse();
  }

  /**
   * Update Course.
   * @param id id of Course to update
   * @param courseDto DTO of Course to update
   * @return updated Course
   */
  @RequestMapping(value = "/courses/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CourseDto updateCourse(@PathVariable("id") UUID id,
      @RequestBody @Valid CourseDto courseDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);

    return moduleService.updateCourse(id, courseDto);
  }

  /**
   * Release Course.
   * @param id id of Course to release
   */
  @RequestMapping(value = "/courses/{id}/release", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public CourseDto releaseCourse(@PathVariable("id") UUID id) {
    Course course = moduleService.findCourseById(id);

    validateCourseForRelease(course);

    return moduleService.releaseCourse(course);
  }

  private void validateCourseForRelease(Course course) {
    BindingResult bindingResult = new BeanPropertyBindingResult(course, COURSE);
    validator.validate(course, bindingResult, CourseReleaseCheck.class);

    checkBindingResult(bindingResult);
  }
}
