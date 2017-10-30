package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class AssignedCourse extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "health_worker_id")
  @Getter
  @Setter
  private CommunityHealthWorker healthWorker;

  @ManyToOne
  @JoinColumn(name = "course_id")
  @Getter
  @Setter
  private Course course;

  @Column(nullable = false)
  @Getter
  @Setter
  private LocalDateTime startDate;

  @Column
  @Getter
  @Setter
  private LocalDateTime endDate;
}
