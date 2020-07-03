package org.motechproject.mots.domain.security;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.BaseTimestampedEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole extends BaseTimestampedEntity {

  @Column(name = "name", nullable = false, unique = true)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_ROLE_NAME)
  private String name;

  @Column(name = "readonly", nullable = false, columnDefinition = "BIT DEFAULT 0")
  @Getter
  @Setter
  private Boolean readonly = false;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role_permissions",
      joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "permission_id", referencedColumnName = "id"))
  @Getter
  @Setter
  @Valid
  private Set<UserPermission> permissions = new HashSet<>();

  public UserRole(String name, Set<UserPermission> permissions) {
    this.name = name;
    this.permissions = permissions;
  }

  public boolean hasPermission(String permissionName) {
    return permissions.stream().anyMatch(permission -> permission.getName().equals(permissionName));
  }
}
