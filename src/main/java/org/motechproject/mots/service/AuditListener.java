package org.motechproject.mots.service;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.motechproject.mots.domain.audit.Auditable;
import org.motechproject.mots.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

  @Value("${mots.loadLocations}")
  private boolean loadLocations;

  static private AuthenticationHelper authenticationHelper;

  @Autowired
  public void init(final AuthenticationHelper authenticationHelper) {
    AuditListener.authenticationHelper = authenticationHelper;
  }

  @PrePersist
  @PreUpdate
  void setOwner(Auditable entity) {
    if (loadLocations) {
      entity.setOwner(authenticationHelper.getAdminUser());
    } else {
      entity.setOwner(authenticationHelper.getCurrentUser());
    }
  }
}
