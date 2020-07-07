package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.enums.Gender;
import org.motechproject.mots.domain.enums.Language;

@Entity
@Table(name = "community_health_worker")
@NoArgsConstructor
@AllArgsConstructor
public class CommunityHealthWorker extends BaseTimestampedEntity {

  @Column(name = "ivr_id", unique = true)
  @Getter
  @Setter
  private String ivrId;

  @Column(name = "chw_id", unique = true, nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_CHW_ID)
  private String chwId;

  @Column(name = "first_name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_FIRST_NAME)
  private String firstName;

  @Column(name = "family_name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_FAMILY_NAME)
  private String familyName;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Gender gender;

  @Column(name = "phone_number", unique = true)
  @Getter
  @Setter
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "village_id")
  @Getter
  @Setter
  private Village village;

  @ManyToOne
  @JoinColumn(name = "facility_id")
  @Getter
  @Setter
  private Facility facility;

  @ManyToOne
  @JoinColumn(name = "sector_id")
  @Getter
  @Setter
  private Sector sector;

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @Getter
  @Setter
  private District district;

  @Column(name = "preferred_language", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Language preferredLanguage;

  @Column(name = "selected", nullable = false, columnDefinition = "BIT NULL DEFAULT 0")
  @Getter
  @Setter
  private Boolean selected = false;

  @ManyToOne
  @JoinColumn(name = "group_id")
  @Getter
  @Setter
  private Group group;

  public CommunityHealthWorker(UUID id) {
    super(id);
  }

  /**
   * Get CHW combined names.
   * @return all CHW names combined into single String
   */
  public String getCombinedName() {
    StringBuilder name = new StringBuilder("");

    if (StringUtils.isNotBlank(getFirstName())) {
      name.append(getFirstName());
    }

    if (StringUtils.isNotBlank(getFamilyName())) {
      name.append(" ").append(getFamilyName());
    }

    return name.toString().trim();
  }
}
