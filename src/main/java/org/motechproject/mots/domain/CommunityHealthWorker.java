package org.motechproject.mots.domain;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.Literacy;

@Entity
@Table(name = "community_health_worker")
@NoArgsConstructor
public class CommunityHealthWorker extends BaseEntity {

  @Column(name = "ivr_id", unique = true)
  @Getter
  @Setter
  private String ivrId;

  @Column(name = "chw_id", unique = true, nullable = false)
  @Getter
  @Setter
  private String chwId;

  @Column(name = "first_name")
  @Getter
  @Setter
  private String firstName;

  @Column(name = "second_name")
  @Getter
  @Setter
  private String secondName;

  @Column(name = "other_name")
  @Getter
  @Setter
  private String otherName;

  @Column(name = "date_of_birth")
  @Getter
  @Setter
  private LocalDate dateOfBirth;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Gender gender;

  @Column(name = "literacy")
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Literacy literacy;

  @Column(name = "education_level")
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private EducationLevel educationLevel;

  @Column(name = "phone_number", unique = true, nullable = false)
  @Getter
  @Setter
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "community_id")
  @Getter
  @Setter
  private Community community;

  @Column(name = "has_peer_supervisor")
  @Getter
  @Setter
  private Boolean hasPeerSupervisor;

  @Column(name = "supervisor")
  @Getter
  @Setter
  private String supervisor;

  @Column(name = "preferred_language")
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Language preferredLanguage;

  public CommunityHealthWorker(UUID id) {
    super(id);
  }
}
