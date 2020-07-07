package org.motechproject.mots.domain.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.BaseTimestampedEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_permission")
public class UserPermission extends BaseTimestampedEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_PERMISSION_NAME)
  private String name;

  @Column(name = "display_name")
  @Getter
  @Setter
  private String displayName;

  @Column(name = "readonly", nullable = false, columnDefinition = "BIT DEFAULT 0")
  @Getter
  @Setter
  private Boolean readonly = false;

  public UserPermission(String name) {
    this.name = name;
  }
}
