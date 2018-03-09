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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;
import org.motechproject.mots.domain.enums.Literacy;
import org.motechproject.mots.validate.annotations.PhoneNumber;

@Entity
@Table(name = "community_health_worker")
@NoArgsConstructor
public class CommunityHealthWorker extends BaseTimestampedEntity {

  @Column(name = "ivr_id", unique = true)
  @Getter
  @Setter
  private String ivrId;

  @Column(name = "chw_id", unique = true, nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_CHW_ID)
  private String chwId;

  @Column(name = "first_name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_FIRST_NAME)
  private String firstName;

  @Column(name = "second_name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_SECOND_NAME)
  private String secondName;

  @Column(name = "other_name")
  @Getter
  @Setter
  private String otherName;

  @Column(name = "date_of_birth")
  @Getter
  @Setter
  private LocalDate dateOfBirth;

  @Column(name = "gender", nullable = false)
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
  @PhoneNumber
  @NotBlank(message = ValidationMessages.EMPTY_PHONE_NUMBER)
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "community_id", nullable = false)
  @Getter
  @Setter
  private Community community;

  @Column(name = "has_peer_supervisor")
  @Getter
  @Setter
  private Boolean hasPeerSupervisor;

  @Column(name = "preferred_language", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Language preferredLanguage;

  public CommunityHealthWorker(UUID id) {
    super(id);
  }

  /**
   * Get CHW combined names.
   * @return all CHW names combined into single String
   */
  public String getCombinedName() {
    String name = "";

    if (StringUtils.isNotBlank(getFirstName())) {
      name = getFirstName();
    }

    if (StringUtils.isNotBlank(getOtherName())) {
      name += " " + getOtherName();
    }

    if (StringUtils.isNotBlank(getSecondName())) {
      name += " " + getSecondName();
    }

    return name.trim();
  }
}
