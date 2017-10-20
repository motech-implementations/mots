package org.motechproject.mots.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.Literacy;

@Entity
@Table
public class HealthWorker extends BaseEntity {

  @Column(unique = true)
  @Getter
  @Setter
  private String ivrId;

  @Column(unique = true, nullable = false)
  @Getter
  @Setter
  private String chwId;

  @Column
  @Getter
  @Setter
  private String firstName;

  @Column
  @Getter
  @Setter
  private String secondName;

  @Column
  @Getter
  @Setter
  private String otherName;

  @Column
  @Getter
  @Setter
  private LocalDate dateOfBirth;

  @Column
  @Getter
  @Setter
  private Gender gender;

  @Column
  @Getter
  @Setter
  private Literacy literacy;

  @Column
  @Getter
  @Setter
  private EducationLevel educationLevel;

  @Column
  @Getter
  @Setter
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "community_id")
  @Getter
  @Setter
  private Community community;

  @Column
  @Getter
  @Setter
  private Boolean peerSupervisor;

  @Getter
  @Setter
  private Language preferredLanguage;
}
