package org.motechproject.mots.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.UserPermission;

@Entity
@Table
public class UserRole extends BaseEntity {

  @Column(nullable = false, unique = true)
  @Getter
  @Setter
  private String name;

  @ElementCollection(targetClass = UserPermission.class, fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @Column
  @Getter
  @Setter
  private Set<UserPermission> permissions = new HashSet<>();
}
