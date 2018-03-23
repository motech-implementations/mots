package org.motechproject.mots.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockAdminUser {

  /**
   * Username of user mock.
   */
  String username() default "admin";

  /**
   * Name of user mock.
   */
  String name() default "Admin";

  /**
   * Email of user mock.
   */
  String email() default "testABCD@mail1234.com";

  /**
   * Password of user mock.
   */
  String password() default "password";

  /**
   * Is enabled value.
   */
  boolean enabled () default true;
}
