package org.motechproject.mots.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.CourseReleaseCheck;

@Entity
@Table(name = "course_module")
@NoArgsConstructor
public class CourseModule extends IvrObject {

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  @Getter
  @Setter
  private Course course;

  @Valid
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "module_id", nullable = false)
  @Getter
  @Setter
  private Module module;

  @NotBlank(message = ValidationMessageConstants.EMPTY_START_MODULE_QUESTION_IVR_ID,
      groups = CourseReleaseCheck.class)
  @Column(name = "start_module_question_ivr_id")
  @Getter
  @Setter
  private String startModuleQuestionIvrId;

  @Column(name = "list_order", nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private CourseModule previousVersion;

  /**
   * Create new CourseModule.
   * @param course parent course
   * @param module related module
   * @param listOrder order of the module in the course
   */
  public CourseModule(Course course, Module module, Integer listOrder) {
    this.course = course;
    this.module = module;
    this.listOrder = listOrder;
  }

  public CourseModule(Course course, Module module) {
    this.course = course;
    this.module = module;
  }

  private CourseModule(String ivrId, String ivrName, Course course, Module module,
      String startModuleQuestionIvrId, Integer listOrder, CourseModule previousVersion) {
    super(ivrId, ivrName);
    this.course = course;
    this.module = module;
    this.startModuleQuestionIvrId = startModuleQuestionIvrId;
    this.listOrder = listOrder;
    this.previousVersion = previousVersion;
  }

  public CourseModule copyAsNewDraft(Course course) {
    return new CourseModule(getIvrId(), getIvrName(), course, module,
        startModuleQuestionIvrId, listOrder, this);
  }
}
