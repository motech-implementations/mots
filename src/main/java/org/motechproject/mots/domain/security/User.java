package org.motechproject.mots.domain.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table
public class User extends BaseEntity implements UserDetails {

  @Column(nullable = false, unique = true)
  @Getter
  @Setter
  private String username;

  @Column
  @Getter
  @Setter
  private String password;

  @Column
  @Getter
  @Setter
  private String email;

  @Column
  @Getter
  @Setter
  private String name;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @Column
  @Getter
  @Setter
  private Set<UserRole> roles = new HashSet<>();

  @Column
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
