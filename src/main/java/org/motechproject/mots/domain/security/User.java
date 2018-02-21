package org.motechproject.mots.domain.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.BaseTimestampedEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
public class User extends BaseTimestampedEntity implements UserDetails {

  @Column(name = "username", nullable = false, unique = true)
  @Getter
  @Setter
  private String username;

  @Column(name = "password")
  @Getter
  @Setter
  private String password;

  @Column(name = "email")
  @Getter
  @Setter
  private String email;

  @Column(name = "name")
  @Getter
  @Setter
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
  @Getter
  @Setter
  private Set<UserRole> roles = new HashSet<>();

  @Column(name = "enabled")
  @Getter
  @Setter
  private Boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(roles.stream()
        .flatMap(userRole -> userRole.getPermissions().stream())
        .map(UserPermission::getRoleName).toArray(String[]::new));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.getEnabled();
  }
}
