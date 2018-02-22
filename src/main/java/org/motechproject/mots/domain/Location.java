package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.enums.FacilityType;

@MappedSuperclass
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class Location extends BaseTimestampedEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NonNull
  @NotBlank(message = ValidationMessages.EMPTY_LOCATION_NAME)
  private String name;

  public Location(UUID id) {
    super(id);
  }

  public abstract String getParentName();

  public FacilityType getFacilityType() {
    return null;
  }

  public String getFacilityId() {
    return null;
  }

  public String getInchargeFullName() {
    return null;
  }
}
