package org.motechproject.mots.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_module")
@NoArgsConstructor
public class CourseModule extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  @Getter
  @Setter
  private Course course;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "module_id", nullable = false)
  @Getter
  @Setter
  private Module module;

  @Column(name = "list_order", nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

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
}
