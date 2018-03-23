package org.motechproject.mots.utils;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.testbuilder.UserDataBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestUtils {

  /**
   * Mock current user.
   */
  public static User createNewUserAndAddToSecurityContext() {
    final User user = new UserDataBuilder().build();

    SecurityContext securityContext = PowerMockito.mock(SecurityContext.class);
    Authentication authentication = PowerMockito.mock(Authentication.class);
    PowerMockito.mockStatic(SecurityContextHolder.class);

    BDDMockito.given(SecurityContextHolder.getContext()).willReturn(securityContext);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(authentication.getPrincipal()).thenReturn(user.getUsername());

    return user;
  }
}