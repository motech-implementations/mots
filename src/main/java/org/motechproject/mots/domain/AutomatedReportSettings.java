package org.motechproject.mots.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

/**
 * These settings are used to send reports to stakeholders via email.
 */
@Entity
@Table(name = "automated_report_settings", uniqueConstraints =
    @UniqueConstraint(columnNames = {"job_name"}))
@AllArgsConstructor
@NoArgsConstructor
public class AutomatedReportSettings extends BaseTimestampedEntity {

  @Column(name = "start_date")
  @Getter
  @Setter
  private Date startDate;

  @Column(name = "interval_in_seconds", nullable = false)
  @Getter
  @Setter
  @Min(60)
  private Integer intervalInSeconds;

  @Column(name = "enabled", nullable = false, columnDefinition = "BIT NULL DEFAULT 0")
  @Getter
  @Setter
  private Boolean enabled = false;

  @Column(name = "job_name", nullable = false)
  @Getter
  @Setter
  private String jobName;

  @Type(type = "text")
  @Column(name = "emails")
  @Getter
  @Setter
  private String emails;

  @Type(type = "text")
  @Column(name = "message_body")
  @Getter
  @Setter
  private String messageBody;

  @Column(name = "message_subject", nullable = false)
  @Getter
  @Setter
  private String messageSubject;
}
