package org.motechproject.mots.domain;

import java.util.ArrayList;
import java.util.List;
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
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.enums.Status;

@Entity
@Table(name = "course")
public class Course extends IvrObject {

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

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_COURSE_NAME)
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

  @OneToOne
  @JoinColumn(name = "previous_version_id")
  @Getter
  @Setter
  private Course previousVersion;

  @OneToOne
  @JoinColumn(name = "no_modules_message_id")
  @Getter
  @Setter
  private Message noModulesMessage;

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

  public List<Module> getReleasedModules() {
    return getModules().stream().filter(module -> module.getStatus().equals(Status.RELEASED))
        .collect(Collectors.toList());
  }
}
