package org.motechproject.mots.domain.audit;

import javax.persistence.PrePersist;
import org.motechproject.mots.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

  private static boolean loadLocations;

  private static AuthenticationHelper authenticationHelper;

  @Autowired
  public void init(final AuthenticationHelper authenticationHelper,
      @Value("${mots.loadLocations}") boolean loadLocations) {
    AuditListener.authenticationHelper = authenticationHelper;
    AuditListener.loadLocations = loadLocations;
  }

  @PrePersist
  protected void setOwner(Auditable entity) {
    if (loadLocations) {
      entity.setOwner(authenticationHelper.getAdminUser());
    } else {
      entity.setOwner(authenticationHelper.getCurrentUser());
    }
  }
}
