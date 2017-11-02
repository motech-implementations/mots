package org.motechproject.mots.domain.security;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.BaseEntity;

@Entity
@Table(name = "user_role")
public class UserRole extends BaseEntity {

  @Column(name = "name", nullable = false, unique = true)
  @Getter
  @Setter
  private String name;

  @ElementCollection(targetClass = UserPermission.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role_permissions",
      joinColumns = @JoinColumn(name = "user_role_id", referencedColumnName = "id"))
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private Set<UserPermission> permissions = new HashSet<>();
}
