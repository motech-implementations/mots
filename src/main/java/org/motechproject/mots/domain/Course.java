package org.motechproject.mots.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.motechproject.mots.validate.CourseReleaseCheck;

@Entity
@Table(name = "course")
public class Course extends IvrObject {

  @Column(name = "name")
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Status status;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
  @OrderBy("list_order ASC")
  @Getter
  @Valid
  private List<CourseModule> courseModules;

  @Column(name = "version", nullable = false)
  @Getter
  @Setter
  private Integer version;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Course previousVersion;

  @NotBlank(message = ValidationMessageConstants.EMPTY_NO_MODULES_MESSAGE_IVR_ID,
      groups = CourseReleaseCheck.class)
  @Column(name = "no_modules_message_ivr_id")
  @Getter
  @Setter
  private String noModulesMessageIvrId;

  @NotBlank(message = ValidationMessageConstants.EMPTY_MENU_INTRO_MESSAGE_IVR_ID,
      groups = CourseReleaseCheck.class)
  @Column(name = "menu_intro_message_ivr_id")
  @Getter
  @Setter
  private String menuIntroMessageIvrId;

  @NotBlank(message = ValidationMessageConstants.EMPTY_CHOOSE_MODULE_QUESTION_IVR_ID,
      groups = CourseReleaseCheck.class)
  @Column(name = "choose_module_question_ivr_id")
  @Getter
  @Setter
  private String chooseModuleQuestionIvrId;

  /**
   * Initialize Course.
   * @return course with initial values
   */
  public static Course initialize() {
    Course course = new Course();
    course.version = 1;
    course.status = Status.DRAFT;

    return course;
  }

  public Course() {
  }

  private Course(String ivrId, String ivrName, String name, String description, Integer version,
      Course previousVersion, String noModulesMessageIvrId, String menuIntroMessageIvrId,
      String chooseModuleQuestionIvrId) {
    super(ivrId, ivrName);
    this.name = name;
    this.description = description;
    this.version = version;
    this.previousVersion = previousVersion;
    this.noModulesMessageIvrId = noModulesMessageIvrId;
    this.menuIntroMessageIvrId = menuIntroMessageIvrId;
    this.chooseModuleQuestionIvrId = chooseModuleQuestionIvrId;

    this.status = Status.DRAFT;
  }

  /**
   * Update list content.
   * @param courseModules list of new Units
   */
  public void setCourseModules(List<CourseModule> courseModules) {
    if (this.courseModules == null) {
      this.courseModules = courseModules;
    } else if (!this.courseModules.equals(courseModules)) {
      this.courseModules.clear();

      if (courseModules != null) {
        this.courseModules.addAll(courseModules);
      }
    }
  }

  /**
   * Get list of modules.
   * @return list of modules
   */
  public List<Module> getModules() {
    if (courseModules == null || courseModules.isEmpty()) {
      return new ArrayList<>();
    }

    return courseModules.stream().map(CourseModule::getModule).collect(Collectors.toList());
  }

  /**
   * Create draft copy of released course.
   * @return draft copy of course
   */
  public Course copyAsNewDraft() {
    Course course = new Course(getIvrId(), getIvrName(), name, description, version + 1, this,
        noModulesMessageIvrId, menuIntroMessageIvrId, chooseModuleQuestionIvrId);

    List<CourseModule> courseModulesCopy = new ArrayList<>();

    if (courseModules != null) {
      courseModules.forEach(courseModule ->
          courseModulesCopy.add(courseModule.copyAsNewDraft(course)));
    }

    course.setCourseModules(courseModulesCopy);
    return course;
  }

  /**
   * Find CourseModule with Module with given id.
   * @param id of Module
   * @return found CourseModule
   */
  public CourseModule findCourseModuleByModuleId(UUID id) {
    if (courseModules == null || courseModules.isEmpty()) {
      throw new EntityNotFoundException("Course has no modules");
    }

    return courseModules.stream().filter(courseModule ->
        courseModule.getModule().getId().equals(id)).findFirst().orElseThrow(() ->
        new EntityNotFoundException("Module with id {0} not found in course draft", id.toString()));
  }

  /**
   * Release the Course.
   */
  public void release() {
    getDraftModules().forEach(Module::release);

    if (!Status.DRAFT.equals(status)) {
      throw new MotsException("Only Course draft can be released");
    }

    status = Status.RELEASED;

    if (previousVersion != null) {
      previousVersion.status = Status.PREVIOUS_VERSION;
    }
  }

  public List<Module> getNewVersionModules() {
    return getModules().stream().filter(module -> Status.DRAFT.equals(module.getStatus())
        && module.getPreviousVersion() != null).collect(Collectors.toList());
  }

  public List<CourseModule> getReleasedCourseModules() {
    return courseModules.stream().filter(courseModule ->
        Status.RELEASED.equals(courseModule.getModule().getStatus())).collect(Collectors.toList());
  }

  private List<Module> getDraftModules() {
    return getModules().stream().filter(module -> Status.DRAFT.equals(module.getStatus())).collect(
        Collectors.toList());
  }
}
