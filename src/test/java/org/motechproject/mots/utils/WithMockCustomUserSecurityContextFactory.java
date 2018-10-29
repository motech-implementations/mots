package org.motechproject.mots.utils;

import java.util.HashSet;
import java.util.Set;
import org.motechproject.mots.constants.DefaultPermissions;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserPermission;
import org.motechproject.mots.domain.security.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockAdminUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockAdminUser customUser) {
    Set<UserPermission> rights = new HashSet<>();
    rights.add(new UserPermission(DefaultPermissions.CREATE_FACILITIES));
    rights.add(new UserPermission(DefaultPermissions.DISPLAY_FACILITIES));
    rights.add(new UserPermission(DefaultPermissions.MANAGE_FACILITIES));

    Set<UserRole> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new UserRole("test-role", rights));

    UserDetails principal = new User(
        customUser.username(),
        customUser.password(),
        customUser.email(),
        customUser.name(),
        grantedAuthorities,
        customUser.enabled()
    );

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        customUser.username(), principal.getPassword(), principal.getAuthorities());
    context.setAuthentication(authentication);

    return context;
  }
}
